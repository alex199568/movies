package com.test.technical.movies.favourites

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
import com.test.technical.movies.favourites.FavouritesAdapter.FavouritesViewHolder

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

class FavouritesAdapter(
    private val context: Context
) : RecyclerView.Adapter<FavouritesViewHolder>() {
  private var removeFromFavouritesCallback: (item: Favourite) -> Unit = { }

  var favourites: List<Favourite> = listOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  fun onRemoveFromFavourites(callback: (item: Favourite) -> Unit) {
    removeFromFavouritesCallback = callback
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.favourite, parent, false)
    return FavouritesViewHolder(itemView)
  }

  override fun getItemCount(): Int = favourites.size

  override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
    holder.bind(favourites[position])
  }

  inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val image = itemView.findViewById<ImageView>(R.id.image)
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val favouritesIndicatorAdded = itemView.findViewById<ImageView>(R.id.favouriteIndicatorAdded)

    fun bind(favourite: Favourite) {
      favouritesIndicatorAdded.setOnClickListener {
        removeFromFavouritesCallback(favourite)
      }

      itemView.setOnClickListener {
        context.startActivity(MovieDetailsActivity.newIntent(context, favourite.movieDbId, true))
      }

      title.text = favourite.title
      Picasso
          .get()
          .load("$IMAGE_BASE_URL${favourite.imagePath}")
          .placeholder(R.drawable.ic_launcher_background) // TODO: find a proper placeholder
          .into(image)
    }
  }
}
