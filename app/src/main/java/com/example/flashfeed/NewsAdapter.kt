package com.example.flashfeed

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.flashfeed.databinding.ArticleListItemBinding

class NewsAdapter(val a: Activity, val articles: ArrayList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(val b: ArticleListItemBinding): ViewHolder(b.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        val bv =ArticleListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(bv)
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        holder.b.articleText.text = articles[position].title

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
            } else {
                holder.b.fav.setImageResource(R.drawable.star_unchecked)
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

    override fun getItemCount() =articles.size
}