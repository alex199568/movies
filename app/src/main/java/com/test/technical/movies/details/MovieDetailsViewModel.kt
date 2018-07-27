package com.test.technical.movies.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.model.MovieDetails
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailsViewModel(
    private val theMovieDBApi: TheMovieDBApi,
    private val favouritesDao: FavouritesDao
) : ViewModel() {
  val movieDetails = MutableLiveData<MovieDetails>()

  fun addToFavourites(details: MovieDetails) {
    Observable.fromCallable {
      favouritesDao.insert(Favourite(details))
    }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }

  fun removeFromFavourites(details: MovieDetails) {
    Observable.fromCallable {
      favouritesDao.deleteByMovieDBId(details.id)
    }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }

  fun requestMovieDetails(movieId: Int) {
    theMovieDBApi
        .details(movieId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { movieDetails.postValue(it) }
  }

  class Factory @Inject constructor(
      private val theMovieDBApi: TheMovieDBApi,
      private val favouritesDao: FavouritesDao
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MovieDetailsViewModel(theMovieDBApi, favouritesDao) as T
  }
}
