package com.example.reoil.api

import com.example.reoil.response.NewsItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("article.json?auth=3arTo1CvZIlOLZHim2qNU7jrY2qX2f1ub336cPWP")
    fun getNews(): Call<Map<String, NewsItem>>

}