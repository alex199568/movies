package com.test.technical.movies.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import kotlinx.android.synthetic.main.fragment_movie_details.responseView
import javax.inject.Inject

private const val MOVIE_ID_EXTRA = "MovieId"

class MovieDetailsFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: MovieDetailsViewModel.Factory

  private lateinit var viewModel: MovieDetailsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel::class.java)

    if (viewModel.movieDetails.value == null) {
      viewModel.requestMovieDetails(arguments?.get(MOVIE_ID_EXTRA) as Int)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_movie_details, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.movieDetails.observe(this, Observer {
      responseView.text = it.toString()
    })
  }

  companion object {
    @JvmStatic
    fun newInstance(movieId: Int): MovieDetailsFragment =
        MovieDetailsFragment().apply {
          arguments = Bundle().apply {
            putInt(MOVIE_ID_EXTRA, movieId)
          }
        }
  }
}
