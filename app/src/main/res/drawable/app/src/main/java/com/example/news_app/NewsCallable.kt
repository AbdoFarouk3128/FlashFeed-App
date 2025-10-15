package com.example.news_app

import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {
    @GET("/v2/top-headlines?country=us&category=general&pageSize=30&apiKey=059f8246f2b54780a9a24595b6dad579")
    fun getNews(): Call<News>

}