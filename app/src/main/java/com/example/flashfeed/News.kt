package com.example.flashfeed

import com.google.gson.annotations.SerializedName


data class News(
    val articles:ArrayList<Article>,

)


data class Article(
    val title:String,
    @SerializedName("url")
    val link:String,
    @SerializedName("urlToImage")
    val image:String,
    var isFavorite:Boolean=false

)
