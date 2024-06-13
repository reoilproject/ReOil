package com.example.reoil.utils

import com.example.reoil.api.ApiService
import com.example.reoil.response.PredictionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ImageClassifierHelper {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://index-kgsrrlgc3a-et.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun classifyImage(file: File, callback: (String) -> Unit) {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        apiService.predictImage(body).enqueue(object : retrofit2.Callback<PredictionResponse> {
            override fun onResponse(call: retrofit2.Call<PredictionResponse>, response: retrofit2.Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.prediction ?: "Unknown")
                } else {
                    callback("Error")
                }
            }

            override fun onFailure(call: retrofit2.Call<PredictionResponse>, t: Throwable) {
                callback("Failure: ${t.message}")
            }
        })
    }
}

