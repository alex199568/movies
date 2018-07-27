package com.test.technical.movies.favourites

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouritesViewModel(private val favouritesDao: FavouritesDao) : ViewModel() {
  fun all(): LiveData<List<Favourite>> = favouritesDao.getAll()

  fun remove(favourite: Favourite) {
    Observable.fromCallable {
      favouritesDao.deleteByMovieDBId(favourite.movieDbId)
    }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }

  class Factory @Inject constructor(private val favouritesDao: FavouritesDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        FavouritesViewModel(favouritesDao) as T
  }
}
