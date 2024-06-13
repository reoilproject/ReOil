package com.example.reoil.utils

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifierHelper(context: Context) {
    private var interpreter: Interpreter

    init {
        val model = FileUtil.loadMappedFile(context, "Minyaq.tflite")
        interpreter = Interpreter(model)
    }

    fun classifyImage(bitmap: Bitmap): String {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)
        val byteBuffer = tensorImage.buffer

        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
        interpreter.run(byteBuffer, outputBuffer.buffer.rewind())

        val results = outputBuffer.floatArray
        val maxIndex = results.indices.maxByOrNull { results[it] } ?: -1

        return when (maxIndex) {
            0 -> "Kotor"
            1 -> "Lumayan Kotor"
            2 -> "Bersih"
            else -> "Tidak Diketahui"
        }
    }
}
