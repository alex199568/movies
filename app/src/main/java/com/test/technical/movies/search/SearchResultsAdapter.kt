package com.test.technical.movies.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.test.technical.movies.R
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.details.MovieDetailsActivity
import com.test.technical.movies.search.SearchResultsAdapter.SearchResultViewHolder

import com.test.technical.movies.model.SearchResult

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

class SearchResultsAdapter(
    private val context: Context,
    private val results: MutableList<SearchResult> = mutableListOf()
) : RecyclerView.Adapter<SearchResultViewHolder>() {
  private var addToFavouritesCallback: (item: SearchResult) -> Unit = { }
  private var removeFromFavouritesCallback: (item: SearchResult) -> Unit = { }

  var favourites: List<Favourite> = listOf()
  set(value) {
    field = value
    notifyDataSetChanged()
  }

  fun onAddToFavourites(callback: (item: SearchResult) -> Unit) {
    addToFavouritesCallback = callback
  }

  fun onRemoveFromFavourites(callback: (item: SearchResult) -> Unit) {
    removeFromFavouritesCallback = callback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.search_result, parent, false)
    return SearchResultViewHolder(itemView)
  }

  override fun getItemCount(): Int = results.size

  override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
    holder.bind(results[position])
  }

  fun add(items: List<SearchResult>) {
    results.addAll(items)
    notifyDataSetChanged()
  }

  fun clear() {
    results.clear()
    notifyDataSetChanged()
  }

  inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val poster = itemView.findViewById<ImageView>(R.id.poster)
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val favouritesIndicatorAdded = itemView.findViewById<ImageView>(R.id.favouriteIndicatorAdded)
    private val favouritesIndicatorRemoved = itemView.findViewById<ImageView>(R.id.favouriteIndicatorRemoved)

    fun bind(result: SearchResult) {
      itemView.setOnClickListener {
        context.startActivity(MovieDetailsActivity.newIntent(context, result.id))
      }

      if (favourites.find { it.movieDbId == result.id } == null) {
        favouritesIndicatorRemoved.visibility = View.VISIBLE
        favouritesIndicatorAdded.visibility = View.GONE
      } else {
        favouritesIndicatorAdded.visibility = View.VISIBLE
        favouritesIndicatorRemoved.visibility = View.GONE
      }

      favouritesIndicatorAdded.setOnClickListener {
        favouritesIndicatorAdded.visibility = View.GONE
        favouritesIndicatorRemoved.visibility = View.VISIBLE
        removeFromFavouritesCallback(result)
      }

      favouritesIndicatorRemoved.setOnClickListener {
        favouritesIndicatorRemoved.visibility = View.GONE
        favouritesIndicatorAdded.visibility = View.VISIBLE
        addToFavouritesCallback(result)
      }

      title.text = result.title
      Picasso
          .get()
          .load("$IMAGE_BASE_URL${result.backdropPath}")
          .placeholder(R.drawable.ic_launcher_background) // TODO: find a proper placeholder
          .into(poster)
    }
  }
}
