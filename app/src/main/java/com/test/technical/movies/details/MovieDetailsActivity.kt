package com.test.technical.movies.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import kotlinx.android.synthetic.main.activity_movie_details.overview
import kotlinx.android.synthetic.main.activity_movie_details.poster
import kotlinx.android.synthetic.main.activity_movie_details.progressBar
import javax.inject.Inject

private const val MOVIE_ID_EXTRA = "MovieId"

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500" // TODO: put it somewhere

class MovieDetailsActivity : AppCompatActivity() {
  @Inject
  lateinit var viewModelFactory: MovieDetailsViewModel.Factory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_details)

    (application as MoviesApp).appComponent.inject(this)

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeButtonEnabled(true)
    }

    val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

    viewModel.movieDetails.observe(this, Observer {
      it?.let {
        progressBar.visibility = View.GONE
        poster.visibility = View.VISIBLE
        overview.visibility = View.VISIBLE
        supportActionBar?.title = it.originalTitle
        Picasso
            .get()
            .load("$IMAGE_BASE_URL${it.posterPath}")
            .placeholder(R.drawable.ic_launcher_background) // TODO: proper placeholder
            .into(poster)
        overview.text = it.overview
      }
    })

    if (viewModel.movieDetails.value == null) {
      viewModel.requestMovieDetails(intent.getIntExtra(MOVIE_ID_EXTRA, 0))
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      android.R.id.home -> {
        onBackPressed(); true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  companion object {
    @JvmStatic
    fun newIntent(context: Context, movieId: Int): Intent =
        Intent(context, MovieDetailsActivity::class.java).apply {
          putExtra(MOVIE_ID_EXTRA, movieId)
        }
  }
}
