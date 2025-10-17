package com.example.flashfeed

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flashfeed.databinding.ActivityMainBinding
import com.example.flashfeed.databinding.ArticleListItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val category = intent.getStringExtra("category")

        val prefs = getSharedPreferences("AppSettingsPrefs", Context.MODE_PRIVATE)
        val country = prefs.getString("preferred_country", "no data")
        Log.d("trace", country!!)


        val apiCountry = when (country) {
            "United States" -> "us"
            "United Kingdom" -> "gb"
            "Egypt" -> "eg"
            else -> {
                "eg"
            }
        }

        loadNews(category!!, apiCountry)


        b.fabUp.setOnClickListener {
            b.newsList.smoothScrollToPosition(0)
        }


        b.swipeRefresh.setOnRefreshListener {
            loadNews(category, apiCountry)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadNews(category: String, country: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val c = retrofit.create(NewsCallable::class.java)
        c.getNews(category = category, country = country).enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                val articles = news?.articles!!
                articles.removeAll {
                    it.title == "[Removed]"
                }
                if (articles.isEmpty()) {
                    b.layoutNoNews.root.visibility = View.VISIBLE
                    b.progress.visibility = View.INVISIBLE
                } else {
                    b.layoutNoNews.root.visibility = View.INVISIBLE
                    showNews(articles)
                    b.progress.visibility = View.INVISIBLE
                    b.swipeRefresh.isRefreshing = false
                }

            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun showNews(article: ArrayList<Article>) {
        val adapter = NewsAdapter(this, article)
        b.newsList.adapter = adapter

    }
}

