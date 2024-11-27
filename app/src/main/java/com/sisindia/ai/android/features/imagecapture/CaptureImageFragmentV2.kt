package com.sisindia.ai.android.features.imagecapture

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.sisindia.ai.android.R
import com.sisindia.ai.android.base.IopsBaseFragment
import com.sisindia.ai.android.databinding.FragmentImageCaptureRevBinding
import com.sisindia.ai.android.room.entities.AttachmentEntity
import com.sisindia.ai.android.utils.FileUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.parceler.Parcels
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CaptureImageFragmentV2 : IopsBaseFragment() {

    lateinit var viewModel: CaptureImageViewModel
    lateinit var binding: FragmentImageCaptureRevBinding
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    lateinit var file: File
    private var isSelfieRequested = false
    private lateinit var selfieTimer: CountDownTimer

    override fun getLayoutResource(): Int = R.layout.fragment_image_capture_rev

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        @JvmStatic
        fun newInstance(): CaptureImageFragmentV2 {
            return CaptureImageFragmentV2()
        }

        @JvmStatic
        fun newInstanceForSelfie(item: AttachmentEntity): CaptureImageFragmentV2 {
            val fragment = CaptureImageFragmentV2()
            fragment.arguments = Bundle().apply {
                putBoolean("IsSelfieRequest", true)
                putParcelable(AttachmentEntity::class.java.simpleName, Parcels.wrap(item))
            }
            return fragment
        }
    }

    override fun extractBundle() {
        if (arguments != null && requireArguments().getBoolean("IsSelfieRequest")) {
            isSelfieRequested = true
            lensFacing = CameraSelector.LENS_FACING_FRONT

            if (requireArguments().containsKey(AttachmentEntity::class.java.simpleName)) {
                val item = Parcels.unwrap<AttachmentEntity>(requireArguments().getParcelable(
                    AttachmentEntity::class.java.simpleName))
                viewModel.itemObs.set(item)
            }
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity(),
            viewModelFactory)[CaptureImageViewModel::class.java]
    }

    override fun initViewBinding(inflater: LayoutInflater, container: ViewGroup): View {
        binding =
            bindFragmentView(layoutResource, inflater, container) as FragmentImageCaptureRevBinding
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        return binding.root
    }

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** Blocking camera operations are performed using this executor */
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override commonMasterData change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == this@CaptureImageFragmentV2.displayId) {
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

    override fun initViewState() {}

    override fun onCreated() {
//        file = viewModel.tempFileForImage
        file = viewModel.getTempFileForImageV2(activity)
        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)
        // Wait for the views to be properly laid out
        binding.viewFinder.post {
            // Keep track of the display in which this view is attached
            if (binding.viewFinder.display != null) displayId = binding.viewFinder.display.displayId
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
        val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = binding.viewFinder.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation).build()

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider) //Todo : Note @Jafar its {Causing build fail: so removed null as parameter}

            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                // We request aspect ratio but no resolution to match preview commonMasterData, but letting
                // CameraX optimize for whatever specific resolution best fits our use cases
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation).setFlashMode(ImageCapture.FLASH_MODE_AUTO).build()

            // ImageAnalysis
            imageAnalyzer = ImageAnalysis.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation, we will have to call this again if rotation changes
                // during the lifecycle of this use case
                .setTargetRotation(rotation).build()

            // Must unbind the use-cases before rebinding them
            cameraProvider.unbindAll()

            try {
                camera = cameraProvider.bindToLifecycle(this,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer)
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

        if (isSelfieRequested) {
            binding.ivSwitchLens.visibility = View.VISIBLE
            binding.ivSelfieTimer.visibility = View.VISIBLE
        }

        // Listener for button used to capture photo
        binding.fabCapture.setOnClickListener {
            if (isSelfieRequested) {
                viewModel.selfieLoader.set(View.VISIBLE)
                captureSelfiePicture()
            } else {
                viewModel.isLoading.set(View.VISIBLE)
                captureOtherTaskPictures()
            }
        }

        binding.ivFlash.setOnClickListener {
            //camera is OFF
            if (camera?.cameraInfo?.torchState?.value == 0)
                camera?.cameraControl?.enableTorch(true)
            else
                camera?.cameraControl?.enableTorch(false)
        }

        binding.ivSelfieTimer.setOnClickListener {
            initSelfieTimer()
            binding.fabCapture.visibility = View.INVISIBLE
        }

        binding.ivSwitchLens.setOnClickListener {
            lensFacing =
                if (CameraSelector.LENS_FACING_BACK == lensFacing) CameraSelector.LENS_FACING_FRONT
                else CameraSelector.LENS_FACING_BACK
            try {
                // Only bind use cases if we can query a camera with this orientation
                //                CameraX.getCameraWithLensFacing(lensFacing)
                bindCameraUseCases()
            } catch (exc: Exception) {
                // Do nothing
            }
        }
    }

    private fun initSelfieTimer() {
        val selfieTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millis: Long) {
                binding.tvSelfieTimer.text =
                    String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis))
            }

            override fun onFinish() {
                captureSelfiePicture()
            }
        }
        selfieTimer.start()
    }

    private fun captureSelfiePicture() {
        imageCapture?.let { imageCapture ->
            if (activity != null && FileUtils.createOrExistsDir(FileUtils.getRootPathV2(activity))) {
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file)
                    .setMetadata(ImageCapture.Metadata()).build()

                imageCapture.takePicture(outputOptions, cameraExecutor, object :
                    ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Timber.d("Photo capture failed: ${exc.message}")
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val compressedImageFile =
                                Compressor.compress(requireActivity(), file) {
                                    // default(format = Bitmap.CompressFormat.JPEG, quality = 65)
                                    default(format = Bitmap.CompressFormat.WEBP, quality = 75)
                                    destination(file)
                                }
                            viewModel.onSelfieImageCaptured(compressedImageFile)
                        }
                    }
                })
            }
        }
    }

    private fun captureOtherTaskPictures() {

        // Get a stable reference of the modifiable image capture use case
        imageCapture?.let { imageCapture ->
            // Create output file to hold the image
//                    if (FileUtils.createOrExistsDir(FileUtils.DIR_ROOT)) {
            if (FileUtils.createOrExistsDir(FileUtils.getRootPathV2(activity))) {
                // Setup image capture metadata
                val metadata = ImageCapture.Metadata().apply {
                    // Mirror image when using the front camera
                    isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                }

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file)
                    .setMetadata(metadata).build()

                // Setup image capture listener which is triggered after photo has been taken
                imageCapture.takePicture(outputOptions, cameraExecutor, object :
                    ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Timber.d("Photo capture failed: ${exc.message}")
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        // val savedUri = output.savedUri ?: Uri.fromFile(file)
                        CoroutineScope(Dispatchers.IO).launch {
                            val compressedImageFile =
                                Compressor.compress(requireActivity(), file) {
                                    default(format = Bitmap.CompressFormat.WEBP,
                                        quality = 75)
                                    // default(format = Bitmap.CompressFormat.JPEG, quality = 70)
                                    destination(file)
                                }
                            viewModel.onImageCaptured(compressedImageFile)
                        }
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::selfieTimer.isInitialized) {
            selfieTimer.cancel()
        }
    }

}