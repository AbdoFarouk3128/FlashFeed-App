package com.example.flashfeed

import Favorites
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.flashfeed.databinding.ActivityFavoriteBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var b: ActivityFavoriteBinding
    private val favList = arrayListOf<Favorites>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setTitle("Favorites")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getFavorites()

        b.fabUp.setOnClickListener {
            b.favoriteList.smoothScrollToPosition(0)
        }


    }

    private fun swipeToDelete(favList: ArrayList<Favorites>) {

        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.bindingAdapterPosition
                showDeleteDialog(position, favList)

            }

        }

        val touchHelper = ItemTouchHelper(callback)

        touchHelper.attachToRecyclerView(b.favoriteList)

    }

    private fun showDeleteDialog(position: Int, favList: ArrayList<Favorites>) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("remove Favorite")
            .setMessage("You will delete this NEWS")
            .setPositiveButton("Yes")
            { _, _ ->
                Firebase.firestore.collection("Favorites")
                    .whereEqualTo("link", favList[position].link)
                    .get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            Firebase.firestore.collection("Favorites").document(doc.id).delete()
                        }
                        Toast.makeText(this, "Removed From Favorites", Toast.LENGTH_SHORT).show()
                    }
                favList.removeAt(position)
                b.favoriteList.adapter?.notifyDataSetChanged()
                if (favList.isEmpty()) {
                    b.layoutNoFavorites.root.visibility = View.VISIBLE
                }

            }
            .setNegativeButton("Cancel")
            { _, _ ->
                b.favoriteList.adapter?.notifyItemChanged(position)
            }
            .setCancelable(false)
            .show()
    }

    private fun getFavorites() {
        favList.clear()
        Firebase.firestore.collection("Favorites")
            .get()
            .addOnSuccessListener { result ->
                favList.clear()
                for (doc in result) {
                    val fav = doc.toObject(Favorites::class.java)
                    favList.add(fav)
                    b.progress.visibility = View.INVISIBLE
                    showFavorites()
                }

                if (favList.isEmpty()) {
                    b.layoutNoFavorites.root.visibility = View.VISIBLE
                    b.progress.visibility = View.INVISIBLE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
            }


    }

    private fun showFavorites() {
        if (favList.isEmpty()) {
            b.progress.visibility = View.INVISIBLE
        } else {
            val adapter = FavoritesAdapter(this, favList)
            b.favoriteList.adapter = adapter
            swipeToDelete(favList)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        getFavorites()
    }


}