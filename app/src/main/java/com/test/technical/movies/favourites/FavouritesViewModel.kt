package com.test.technical.movies.favourites

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.util.SchedulersWrapper
import io.reactivex.Observable
import javax.inject.Inject

class FavouritesViewModel(
    private val favouritesDao: FavouritesDao,
    private val schedulers: SchedulersWrapper
) : ViewModel() {
  fun all(): LiveData<List<Favourite>> = favouritesDao.getAll()

  fun remove(favourite: Favourite) {
    Observable.fromCallable {
      favouritesDao.deleteByMovieDBId(favourite.movieDbId)
    }
        .subscribeOn(schedulers.io)
        .subscribe()
  }

  class Factory @Inject constructor(
      private val favouritesDao: FavouritesDao,
      private val schedulers: SchedulersWrapper
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        FavouritesViewModel(favouritesDao, schedulers) as T
  }
}
