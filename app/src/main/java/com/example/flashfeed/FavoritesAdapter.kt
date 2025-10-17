package com.example.flashfeed

import Favorites
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.flashfeed.databinding.FavoritesListBinding

class FavoritesAdapter(private val favList: ArrayList<Favorites>) :
    RecyclerView.Adapter<FavoritesAdapter.FavViewHolder>() {

    class FavViewHolder(val b: FavoritesListBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val b = FavoritesListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavViewHolder(b)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.b.titleTv.text = favList[position].title
        holder.b.linkTv.text=favList[position].link
        holder.b.linkTv.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, favList[position].link.toUri())
            holder.itemView.context.startActivity(i)
        }
    }

    override fun getItemCount() = favList.size
}
