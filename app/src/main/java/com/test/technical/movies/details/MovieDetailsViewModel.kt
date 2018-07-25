package com.test.technical.movies.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.model.MovieDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel(private val theMovieDBApi: TheMovieDBApi) : ViewModel() {
  val movieDetails = MutableLiveData<MovieDetails>()

  fun requestMovieDetails(movieId: Int) {
    theMovieDBApi
        .details(movieId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { movieDetails.postValue(it) }
  }

  class Factory @Inject constructor(private val theMovieDBApi: TheMovieDBApi) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MovieDetailsViewModel(theMovieDBApi) as T
  }
}
