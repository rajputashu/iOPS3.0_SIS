package com.sisindia.ai.android.features.livecamera

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.constants.NavigationConstants
import com.sisindia.ai.android.databinding.FragmentCaptureImageBinding
import com.sisindia.ai.android.utils.FileUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** Helper type alias used for analysis use case callbacks */
typealias LumaListener = (luma: Double) -> Unit

class CaptureImageFragment : IopsBaseFragment() {

    private var binding: FragmentCaptureImageBinding? = null
    private var activityViewModel: LiveImageCameraViewModel? = null
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    lateinit var file: File

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** Blocking camera operations are performed using this executor */
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CaptureImageFragment.displayId) {
                //                Timber.d("Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Shut down our background executor
        cameraExecutor.shutdown()
        // Unregister the broadcast receivers and listeners
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun extractBundle() {
        if (arguments != null && requireArguments().containsKey(File::class.java.simpleName)) {
            file = requireArguments().getSerializable(File::class.java.simpleName) as File

            if (requireArguments().containsKey("IsSelfieRequest"))
                lensFacing = CameraSelector.LENS_FACING_FRONT
        } else {
            Toast.makeText(activity, "failed to create a file for Image...!!!", Toast.LENGTH_SHORT)
                .show()
            requireActivity().onBackPressed()
        }
    }

    override fun initViewModel() {
        if (activity != null)
            activityViewModel = ViewModelProvider(requireActivity(), viewModelFactory)
                .get(LiveImageCameraViewModel::class.java)
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
        binding!!.lifecycleOwner = viewLifecycleOwner
        binding!!.vm = activityViewModel
        binding!!.executePendingBindings()
        return binding!!.root
    }

    override fun initViewState() {

    }

    override fun onCreated() {
        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)
        // Wait for the views to be properly laid out
        binding!!.viewFinder.post {
            // Keep track of the display in which this view is attached
            displayId = binding!!.viewFinder.display.displayId
            // Build UI controls
            updateCameraUi()
            // Bind use cases
            bindCameraUseCases()
        }
    }

    /**
     * Inflate camera controls and update the UI manually upon commonMasterData changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateCameraUi()
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { binding!!.viewFinder.display.getRealMetrics(it) }
        //        Timber.d("Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        //        Timber.d("Preview aspect ratio: $screenAspectRatio")

        val rotation = binding!!.viewFinder.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

            // Attach the viewfinder's surface provider to preview use case

            preview?.setSurfaceProvider(binding!!.viewFinder.surfaceProvider)

            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview commonMasterData, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .build()

            // ImageAnalysis
            imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(
                        cameraExecutor,
                        LuminosityAnalyzer { luma ->
                            // Values returned from our analyzer are passed to the attached listener
                            // We log image analysis results here - you should do something useful
                            // instead!
                            Timber.d("Average luminosity: $luma")
                        }
                    )
                }

            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()

            try {
                // A variable number of use-cases can be passed here -
                // camera provides access to CameraControl & CameraInfo
                camera = cameraProvider.bindToLifecycle(this,
                    cameraSelector, preview, imageCapture, imageAnalyzer)
            } catch (exc: Exception) {
                Timber.d("Use case binding failed $exc")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /**
     *  [androidx.camera.core.ImageAnalysisConfig] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */
    private fun updateCameraUi() {

        // Listener for button used to capture photo
        binding!!.fabCapture.setOnClickListener {

            activityViewModel!!.isLoading.set(View.VISIBLE)

            // Get a stable reference of the modifiable image capture use case
            imageCapture?.let { imageCapture ->

                // Create output file to hold the image
//                if (FileUtils.createOrExistsDir(DIR_ROOT)) {
                if (FileUtils.createOrExistsDir(FileUtils.getRootPathV2(activity))) {

                    val metadata = ImageCapture.Metadata().apply {
                        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                    }

                    // Create output options object which contains file + metadata
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file)
                        .setMetadata(metadata)
                        .build()

                    // Setup image capture listener which is triggered after photo has been taken
                    imageCapture.takePicture(outputOptions,
                        cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                            override fun onError(exc: ImageCaptureException) {
                                Timber.d("Photo capture failed: ${exc.message}")
                            }

                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                //                                val savedUri = output.savedUri ?: Uri.fromFile(file)
                                CoroutineScope(Dispatchers.IO).launch {
                                    val compressedImageFile =
                                        Compressor.compress(requireActivity(), file) {
                                            default(format = Bitmap.CompressFormat.JPEG,
                                                quality = 70)
                                            destination(file)
                                        }
                                    val fileSize = compressedImageFile.length() / 1024
                                    //                                    Timber.e("Compressed Size :${fileSize} KB")
                                    val compressedUri = Uri.fromFile(compressedImageFile)
                                    activityViewModel!!.isLoading.set(View.GONE)
                                    vMessage.what = NavigationConstants.ON_IMAGE_CAPTURED
                                    vMessage.obj = compressedUri
                                    vMessage.arg1 = fileSize.toInt()
                                    liveData.postValue(vMessage)
                                }
                            }
                        })
                }
            }
        }
    }

    /**
     * Our custom image analysis class.
     *
     * <p>All we need to do is override the function `analyze` with our desired operations. Here,
     * we compute the average luminosity of the image by looking at the Y plane of the YUV frame.
     */
    private class LuminosityAnalyzer(listener: LumaListener? = null) : ImageAnalysis.Analyzer {
        private val frameRateWindow = 8
        private val frameTimestamps = ArrayDeque<Long>(5)
        private val listeners = ArrayList<LumaListener>().apply { listener?.let { add(it) } }
        private var lastAnalyzedTimestamp = 0L
        var framesPerSecond: Double = -1.0
            private set

        /**
         * Used to add listeners that will be called with each luma computed
         */
        fun onFrameAnalyzed(listener: LumaListener) = listeners.add(listener)

        /**
         * Helper extension function used to extract a byte array from an image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        /**
         * Analyzes an image to produce a result.
         *
         * <p>The caller is responsible for ensuring this analysis method can be executed quickly
         * enough to prevent stalls in the image acquisition pipeline. Otherwise, newly available
         * images will not be acquired and analyzed.
         *
         * <p>The image passed to this method becomes invalid after this method returns. The caller
         * should not store external references to this image, as these references will become
         * invalid.
         *
         * @param image image being analyzed VERY IMPORTANT: Analyzer method implementation must
         * call image.close() on received images when finished using them. Otherwise, new images
         * may not be received or the camera may stall, depending on back pressure setting.
         *
         */
        override fun analyze(image: ImageProxy) {
            // If there are no listeners attached, we don't need to perform analysis
            if (listeners.isEmpty()) {
                image.close()
                return
            }

            // Keep track of frames analyzed
            val currentTime = System.currentTimeMillis()
            frameTimestamps.push(currentTime)

            // Compute the FPS using a moving average
            while (frameTimestamps.size >= frameRateWindow) frameTimestamps.removeLast()
            val timestampFirst = frameTimestamps.peekFirst() ?: currentTime
            val timestampLast = frameTimestamps.peekLast() ?: currentTime
            framesPerSecond = 1.0 / ((timestampFirst - timestampLast) /
                    frameTimestamps.size.coerceAtLeast(1).toDouble()) * 1000.0

            // Analysis could take an arbitrarily long amount of time
            // Since we are running in a different thread, it won't stall other use cases

            lastAnalyzedTimestamp = frameTimestamps.first

            // Since format in ImageAnalysis is YUV, image.planes[0] contains the luminance plane
            val buffer = image.planes[0].buffer

            // Extract image data from callback object
            val data = buffer.toByteArray()

            // Convert the data into an array of pixel values ranging 0-255
            val pixels = data.map { it.toInt() and 0xFF }

            // Compute average luminance for the image
            val luma = pixels.average()

            // Call all listeners with new value
            listeners.forEach { it(luma) }

            image.close()
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_capture_image
    }

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        fun newInstance(imageFile: File): Fragment {
            val bundle = Bundle()
            bundle.putSerializable(File::class.java.simpleName, imageFile)
            val fragment = CaptureImageFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstanceWithFront(imageFile: File): Fragment {
            val fragment = CaptureImageFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(File::class.java.simpleName, imageFile)
                putBoolean("IsSelfieRequest", true)
            }
            return fragment
        }
    }
}
