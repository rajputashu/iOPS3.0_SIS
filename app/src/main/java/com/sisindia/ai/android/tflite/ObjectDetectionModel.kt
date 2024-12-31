package com.sisindia.ai.android.tflite

import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.sisindia.ai.android.tflite.Constants.HUMAN_LABELS_PATH
import com.sisindia.ai.android.tflite.Constants.HUMAN_MODEL_PATH
import java.io.BufferedReader
import java.io.InputStreamReader

class ObjectDetectionModel(private val context: Context) {
    private lateinit var tflite: Interpreter
    private lateinit var labels: List<String>

    // Load model and labels
    fun initializeModel() {
        // Load the TensorFlow Lite model
        val assetManager = context.assets
//        val modelPath = "model.tflite"
//        val labelsPath = "labels.txt"

        // Load the labels from the labels.txt file
        labels = loadLabels(assetManager, HUMAN_LABELS_PATH)

        // Load the TFLite model
        val model = assetManager.openFd(HUMAN_MODEL_PATH)
        val modelByteBuffer = ByteBuffer.allocateDirect(model.length.toInt())
        model.createInputStream().use { input ->
            input.read(modelByteBuffer.array())
        }

        // Create a new Interpreter from the model
        tflite = Interpreter(modelByteBuffer)
    }

    // Load the labels from a file
    private fun loadLabels(assetManager: AssetManager, labelsPath: String): List<String> {
        val labels = mutableListOf<String>()
        val inputStream = assetManager.open(labelsPath)
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { lines ->
            lines.forEach { labels.add(it) }
        }
        return labels
    }

    // Perform object detection on an image
    fun detectObjects(bitmap: Bitmap): List<DetectionResult> {
        val inputBuffer = preprocessImage(bitmap)

        // Create an output array to hold detection results
        val outputBuffer = Array(1) { Array(10) { FloatArray(4) } }  // Modify the size based on your model's output format

        // Run inference
        tflite.run(inputBuffer, outputBuffer)

        return processResults(outputBuffer)
    }

    // Preprocess the image to match model input size
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val inputSize = 300  // Modify based on your model's input size
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Resize and normalize the image (for example, using a TensorFlow Lite pre-processing utility)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixel = resizedBitmap.getPixel(i, j)
                byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f)
                byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)
                byteBuffer.putFloat((pixel and 0xFF) / 255.0f)
            }
        }

        return byteBuffer
    }

    // Process results from the model and return bounding boxes
    private fun processResults(outputBuffer: Array<Array<FloatArray>>): List<DetectionResult> {
        val results = mutableListOf<DetectionResult>()
        for (i in outputBuffer[0].indices) {
            val score = outputBuffer[0][i][0]
            if (score > 0.5f) {  // Use a threshold for confidence
                val x1 = outputBuffer[0][i][1]
                val y1 = outputBuffer[0][i][2]
                val x2 = outputBuffer[0][i][3]
                val y2 = outputBuffer[0][i][4]
                val label = labels[i]

                results.add(DetectionResult(label, score, x1, y1, x2, y2))
            }
        }
        return results
    }

    // Result class to hold detection data
    data class DetectionResult(
        val label: String,
        val score: Float,
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float
    )
}
