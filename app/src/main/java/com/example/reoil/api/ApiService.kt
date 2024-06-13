package com.example.reoil.api

import com.example.reoil.response.NewsItem
import com.example.reoil.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET("article.json?auth=3arTo1CvZIlOLZHim2qNU7jrY2qX2f1ub336cPWP")
    fun getNews(): Call<Map<String, NewsItem>>

    @Multipart
    @POST("predict")
    fun predictImage(@Part file: MultipartBody.Part): Call<PredictionResponse>

}