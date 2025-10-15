package com.example.flashfeed

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {
    @GET("v2/top-headlines")
    fun getNews(
        @Query("country") country: String = "us",
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int = 30,
        @Query("apiKey") apiKey: String = "059f8246f2b54780a9a24595b6dad579"
    ): Call<News>
}

///v2/top-headlines?country=us&category=general&pageSize=30&apiKey=059f8246f2b54780a9a24595b6dad579

//

