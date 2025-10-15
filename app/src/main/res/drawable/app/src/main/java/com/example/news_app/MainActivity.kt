
package com.example.news_app

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.news_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b=ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadNews()
        b.fabUp.setOnClickListener {
            b.newsList.smoothScrollToPosition(0)
        }
        b.swipeRefresh.setOnRefreshListener {
            loadNews()

        }

    }

    private fun loadNews(){
        val retrofit=Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val c = retrofit.create(NewsCallable::class.java)
        c.getNews().enqueue(object : Callback<News>{
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news =response.body()
                val articles=news?.articles!!
                articles.removeAll{
                    it.title=="[Removed]"
                }
                showNews(articles)
                b.progress.isVisible= false
                b.swipeRefresh.isRefreshing=false
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                b.progress.isVisible= false
            }

        })
    }
    private fun showNews(articles:ArrayList<Article>){
        val adapter =NewsAdapter(this,articles)
        b.newsList.adapter=adapter

    }
}