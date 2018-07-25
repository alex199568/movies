package com.test.technical.movies.favourites

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.technical.movies.R
import com.test.technical.movies.favourites.FavouritesAdapter.FavouritesViewHolder

class FavouritesAdapter(private val context: Context) : RecyclerView.Adapter<FavouritesViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.favourite, parent, false)
    return FavouritesViewHolder(itemView)
  }

  override fun getItemCount(): Int = 0

  override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
    holder.bind()
  }

  inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind() { }
  }
}
