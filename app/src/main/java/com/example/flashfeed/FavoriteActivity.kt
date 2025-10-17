package com.example.flashfeed

import Favorites
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flashfeed.databinding.ActivityFavoriteBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var b:ActivityFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b=ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setTitle("Favorites")
        showFavorites()

    }

    private fun showFavorites(){
        val favList = arrayListOf<Favorites>()
        val adapter = FavoritesAdapter(favList)
        b.favoriteList.adapter = adapter
        Firebase.firestore.collection("Favorites")
            .get()
            .addOnSuccessListener { result ->
                favList.clear()
                for (doc in result) {
                    val fav = doc.toObject(Favorites::class.java)
                    favList.add(fav)
                    b.progress.visibility=View.INVISIBLE
                }
                adapter.notifyDataSetChanged()
            }


    }
}