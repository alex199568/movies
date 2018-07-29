package com.test.technical.movies.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.model.SearchResponse
import com.test.technical.movies.model.SearchResult
import com.test.technical.movies.util.SchedulersWrapper
import io.reactivex.Observable
import javax.inject.Inject

class SearchViewModel(
    private val theMovieDBApi: TheMovieDBApi,
    private val favouritesDao: FavouritesDao,
    private val schedulers: SchedulersWrapper
) : ViewModel() {
  val searchResponse = MutableLiveData<SearchResponse>()

  private var onErrorCallback: () -> Unit = { }

  fun onError(callback: () -> Unit) {
    onErrorCallback = callback
  }

  fun search(query: String, page: Int) {
    theMovieDBApi
        .search(query, page)
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe({ searchResponse.postValue(it) }, { onErrorCallback() })
  }

  fun favourites(): LiveData<List<Favourite>> = favouritesDao.getAll()

  fun addToFavourites(result: SearchResult) {
    Observable.fromCallable {
      favouritesDao.insert(Favourite(result))
    }
        .subscribeOn(schedulers.io)
        .subscribe()
  }

  fun removeFromFavourites(result: SearchResult) {
    Observable.fromCallable {
      favouritesDao.deleteByMovieDBId(result.id)
    }
        .subscribeOn(schedulers.io)
        .subscribe()
  }

  class Factory @Inject constructor(
      private val theMovieDBApi: TheMovieDBApi,
      private val favouritesDao: FavouritesDao,
      private val schedulers: SchedulersWrapper
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SearchViewModel(theMovieDBApi, favouritesDao, schedulers) as T
  }
}
