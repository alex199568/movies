package com.test.technical.movies.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import com.test.technical.movies.model.MovieDetails
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_movie_details.coordinator
import kotlinx.android.synthetic.main.activity_movie_details.overview
import kotlinx.android.synthetic.main.activity_movie_details.poster
import kotlinx.android.synthetic.main.activity_movie_details.progressBar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val MOVIE_ID_EXTRA = "MovieId"
private const val IS_FAVOURITE_EXTRA = "IsFavourite"

private const val REQUEST_RETRY_INTERVAL = 5L

class MovieDetailsActivity : AppCompatActivity() {
  @Inject
  lateinit var viewModelFactory: MovieDetailsViewModel.Factory

  private lateinit var viewModel: MovieDetailsViewModel

  private var isFavourite = false

  private var details: MovieDetails? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_details)

    (application as MoviesApp).appComponent.inject(this)

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

    viewModel.movieDetails.observe(this, Observer {
      it?.let {
        details = it
        progressBar.visibility = View.GONE
        poster.visibility = View.VISIBLE
        overview.visibility = View.VISIBLE
        supportActionBar?.title = it.originalTitle
        Picasso
            .get()
            .load("${getString(R.string.images_base_url)}${it.posterPath}")
            .placeholder(R.drawable.poster_placeholder)
            .into(poster)
        overview.text = it.overview
      }
    })

    viewModel.onError {
      Snackbar.make(coordinator, getString(R.string.offlineMessage), Snackbar.LENGTH_SHORT).show()
      Observable.timer(REQUEST_RETRY_INTERVAL, TimeUnit.SECONDS).subscribe { requestMovieDetails() }
    }

    if (viewModel.movieDetails.value == null) {
      requestMovieDetails()
    }

    isFavourite = intent.getBooleanExtra(IS_FAVOURITE_EXTRA, false)
  }

  private fun requestMovieDetails() {
    viewModel.requestMovieDetails(intent.getIntExtra(MOVIE_ID_EXTRA, 0))
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.movie_details_menu, menu)
    viewModel.movieDetails.observe(this, Observer { menu?.let{ initFavouriteIcon(it) } })
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed(); true
      }
      R.id.toggleFavourite -> {
        details?.let {
          if (isFavourite) {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_24dp)
            viewModel.removeFromFavourites(it)
          } else {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_24dp)
            viewModel.addToFavourites(it)
          }
          isFavourite = !isFavourite
        }
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun initFavouriteIcon(menu: Menu) {
    val item = menu.findItem(R.id.toggleFavourite)
    item.isVisible = true
    if (isFavourite) {
      item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_24dp)
    } else {
      item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_24dp)
    }
  }

  companion object {
    @JvmStatic
    fun newIntent(context: Context, movieId: Int, isFavourite: Boolean): Intent =
        Intent(context, MovieDetailsActivity::class.java).apply {
          putExtra(MOVIE_ID_EXTRA, movieId)
          putExtra(IS_FAVOURITE_EXTRA, isFavourite)
        }
  }
}
