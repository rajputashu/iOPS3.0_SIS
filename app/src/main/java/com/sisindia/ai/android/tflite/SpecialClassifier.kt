package com.sisindia.ai.android.tflite

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class SpecialClassifier(private val context: Context,
                        private val modelPath: String,
                        private val classifierListener: ClassifierListener) {
    private var interpreter: Interpreter? = null
    private var tensorWidth = 0
    private var tensorHeight = 0
    private var numClass = 0
    private var labels: List<String> = emptyList()

    // Define the class mappings
    // Define the class mappings
    private val classificationMaps =
        mapOf("SHIRT" to mapOf("SIS-SHIRT" to setOf(14),    // Index 14 in model output
            "NON-SIS-SHIRT" to setOf(9)  // Index 9 in model output
        ), "PANT" to mapOf("SIS-PANT" to setOf(13),      // Index 13 in model output
            "NON-SIS-PANT" to setOf(8)   // Index 8 in model output
        ), "SHOE" to mapOf("SHOE-GOOD" to setOf(11),     // Index 11 in model output
            "SHOE-POOR" to setOf(12), // Index 12 in model output
            "NO SHOE" to setOf(7)// Index 7 in model output
        ), "JACKET" to mapOf("JACKET-TYPE-1" to setOf(3), // Index 3 in model output
            "JACKET-TYPE-2" to setOf(4), // Index 4 in model output
            "JACKET-TYPE-3" to setOf(5)  // Index 5 in model output
        ), "FACE" to mapOf("BEARDED" to setOf(0),      // Index 0 in model output
            "SHAVED" to setOf(10)   // Index 10 in model output
        ), "HAIR" to mapOf("HAIR-PROPER" to setOf(2),     // Index 2 in model output
            "HAIR-IMPROPER" to setOf(1), // Index 1 in model output
            "NO-HAIR" to setOf(6)// Index 6 in model output
        ))

    private val imageProcessor =
        ImageProcessor.Builder().add(NormalizeOp(INPUT_MEAN, INPUT_STANDARD_DEVIATION))
            .add(CastOp(INPUT_IMAGE_TYPE)).build()

    fun setup() {
        if (interpreter != null) {
            close()
        }

        try {
            labels = context.assets.open("labels1.txt").bufferedReader().useLines { it.toList() }
            if (labels.size != 12) {
                Log.e("SpecialClassifier", "Expected 12 labels but found ${labels.size}")
            }
        } catch (e: IOException) {
            Log.e("SpecialClassifier", "Error loading labels: ${e.message}")
            return
        }

        val options = Interpreter.Options().apply {
            this.setNumThreads(4)
        }

        val model = FileUtil.loadMappedFile(context, modelPath)
        interpreter = Interpreter(model, options)

        val inputShape = interpreter?.getInputTensor(0)?.shape() ?: return
        val outputShape = interpreter?.getOutputTensor(0)?.shape() ?: return

        tensorWidth = if (inputShape[1] == 3) inputShape[2] else inputShape[1]
        tensorHeight = if (inputShape[1] == 3) inputShape[3] else inputShape[2]
        numClass = outputShape[1]

        Log.d("SpecialClassifier", "Model initialized with output classes: $numClass")
    }

    fun classify(frame: Bitmap, objectClass: String) {
        interpreter ?: return
        if (tensorWidth == 0 || tensorHeight == 0) return

        Log.d("SpecialClassifier", "Starting classification for $objectClass")

        var inferenceTime = SystemClock.uptimeMillis()

        try {
            // Process image
            val tensorImage = TensorImage(INPUT_IMAGE_TYPE)
            tensorImage.load(frame)
            val processedImage = imageProcessor.process(tensorImage)

            // Prepare output buffer
            val output = TensorBuffer.createFixedSize(intArrayOf(1, numClass), OUTPUT_IMAGE_TYPE)

            // Run inference
            interpreter?.run(processedImage.buffer, output.buffer)
            val outputArray = output.floatArray

            // Debug log: Print all outputs
            Log.d("SpecialClassifier", "Raw model outputs: ${outputArray.contentToString()}")

            // Get the relevant class mapping for this object type
            val classMap = classificationMaps[objectClass.uppercase()]
            if (classMap == null) {
                Log.e("SpecialClassifier", "No class mapping found for $objectClass")
                return
            }

            // Debug log: Print relevant class mapping
            Log.d("SpecialClassifier", "Class mapping for $objectClass: $classMap")

            // Find the highest confidence among the relevant classes for this object type
            var maxConf = Float.NEGATIVE_INFINITY
            var classifiedAs = "Unknown"
            var debugConfidences = mutableMapOf<String, Float>()

            classMap.forEach { (className, indices) ->
                val confidence = indices.maxOfOrNull { outputArray[it] } ?: Float.NEGATIVE_INFINITY
                debugConfidences[className] = confidence
                if (confidence > maxConf) {
                    maxConf = confidence
                    classifiedAs = className
                }
            }

            // Debug log: Print confidences for each class
            Log.d("SpecialClassifier", "Confidences for $objectClass: $debugConfidences")

            inferenceTime = SystemClock.uptimeMillis() - inferenceTime

            Log.d("SpecialClassifier",
                "Final classification for $objectClass: $classifiedAs with confidence $maxConf")

            // Return result through listener
            classifierListener.onClassification(ClassificationResult(originalClass = objectClass,
                classifiedAs = classifiedAs,
                confidence = maxConf,
                inferenceTime = inferenceTime))
        } catch (e: Exception) {
            Log.e("SpecialClassifier", "Error during classification of $objectClass: ${e.message}")
            e.printStackTrace()
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    data class ClassificationResult(val originalClass: String,
                                    val classifiedAs: String,
                                    val confidence: Float,
                                    val inferenceTime: Long)

    interface ClassifierListener {
        fun onClassification(result: ClassificationResult)
    }

    companion object {
        private const val INPUT_MEAN = 0f
        private const val INPUT_STANDARD_DEVIATION = 255f
        private val INPUT_IMAGE_TYPE = DataType.FLOAT32
        private val OUTPUT_IMAGE_TYPE = DataType.FLOAT32
        private const val CONFIDENCE_THRESHOLD = 0.5F
    }
}