package com.test.technical.movies.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import kotlinx.android.synthetic.main.activity_movie_details.responseView
import javax.inject.Inject

private const val MOVIE_ID_EXTRA = "MovieId"

class MovieDetailsActivity : AppCompatActivity() {
  @Inject
  lateinit var viewModelFactory: MovieDetailsViewModel.Factory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie_details)

    (application as MoviesApp).appComponent.inject(this)

    val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

    viewModel.movieDetails.observe(this, Observer {
      responseView.text = it.toString()
    })

    if (viewModel.movieDetails.value == null) {
      viewModel.requestMovieDetails(intent.getIntExtra(MOVIE_ID_EXTRA, 0))
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
