package com.example.flashfeed

import Favorites
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.flashfeed.databinding.ArticleListItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.FirebaseAuth // Added import for FirebaseAuth

class NewsAdapter(val a: Activity, val articles: ArrayList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    lateinit var favList: List<String>

    class NewsViewHolder(val b: ArticleListItemBinding) : ViewHolder(b.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        val bv = ArticleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(bv)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        holder.b.articleText.text = articles[position].title
        
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.w("NewsAdapter", "User not logged in, cannot load favorites.")
            // Optionally, disable favorite button or show a message
        } else {
            Firebase.firestore.collection("Users").document(userId).collection("Favorites")
                .get()
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        val fav = doc.toObject(Favorites::class.java)
                        if (fav.title == articles[position].title) {
                            holder.b.fav.setImageResource(R.drawable.star_checked)
                            articles[position].isFavorite = true
                        }
                    }
                }
        }


        Glide.with(holder.b.articleImage.context)
            .load(articles[position].image)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.b.articleImage)

        val url = articles[position].link
        holder.b.articleContainer.setOnClickListener {

            val i = Intent(Intent.ACTION_VIEW, url.toUri())
            a.startActivity(i)

        }
        if (articles[position].isFavorite) {
            holder.b.fav.setImageResource(R.drawable.star_checked)
        } else {
            holder.b.fav.setImageResource(R.drawable.star_unchecked)
        }

        holder.b.fav.setOnClickListener {
            articles[position].isFavorite = !articles[position].isFavorite

            if (articles[position].isFavorite) {
                holder.b.fav.setImageResource(R.drawable.star_checked)
                val title = articles[position].title
                val link = articles[position].link
                val fav = Favorites(title = title, link = link)
                Log.d("trace", "Trying to add fav")
                
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId == null) {
                    Log.w("NewsAdapter", "User not logged in, cannot add to favorites.")
                    Toast.makeText(a, "Please log in to add favorites", Toast.LENGTH_SHORT).show()
                    // Revert the favorite state in UI if not logged in
                    articles[position].isFavorite = false
                    holder.b.fav.setImageResource(R.drawable.star_unchecked)
                } else {
                    Firebase.firestore.collection("Users").document(currentUserId).collection("Favorites").add(fav)
                        .addOnSuccessListener {
                            it.update("id", it.id)
                                .addOnSuccessListener {
                                    Toast.makeText(a, "Added To Favorites", Toast.LENGTH_SHORT).show()
                                }
                        }
                }

            } else if (!articles[position].isFavorite) {
                holder.b.fav.setImageResource(R.drawable.star_unchecked)
                
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId == null) {
                    Log.w("NewsAdapter", "User not logged in, cannot remove from favorites.")
                    Toast.makeText(a, "Please log in to remove favorites", Toast.LENGTH_SHORT).show()
                    // Revert the favorite state in UI if not logged in
                    articles[position].isFavorite = true
                    holder.b.fav.setImageResource(R.drawable.star_checked)
                } else {
                    Firebase.firestore.collection("Users").document(currentUserId).collection("Favorites")
                        .whereEqualTo("link", articles[position].link)
                        .get()
                        .addOnSuccessListener { result ->
                            for (doc in result) {
                                Firebase.firestore.collection("Users").document(currentUserId).collection("Favorites").document(doc.id).delete()
                            }
                            Toast.makeText(a, "Removed From Favorites", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        holder.b.shareFab.setOnClickListener {
            ShareCompat.IntentBuilder(a)
                .setType("text/plain")
                .setChooserTitle("Share article with:")
                .setText(url)
                .startChooser()
        }

    }

    override fun getItemCount() = articles.size
}