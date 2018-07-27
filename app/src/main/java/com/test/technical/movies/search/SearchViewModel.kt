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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(
    private val theMovieDBApi: TheMovieDBApi,
    private val favouritesDao: FavouritesDao
) : ViewModel() {
  val searchResponse = MutableLiveData<SearchResponse>()

  fun search(query: String, page: Int) {
    theMovieDBApi
        .search(query, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          searchResponse.postValue(it)
        }
  }

  fun favourites(): LiveData<List<Favourite>> = favouritesDao.getAll()

  fun addToFavourites(result: SearchResult) {
    Observable.fromCallable {
      favouritesDao.insert(Favourite(result))
    }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }

  fun removeFromFavourites(result: SearchResult) {
    Observable.fromCallable {
      favouritesDao.deleteByMovieDBId(result.id)
    }
        .subscribeOn(Schedulers.io())
        .subscribe()
  }

  class Factory @Inject constructor(
      private val theMovieDBApi: TheMovieDBApi,
      private val favouritesDao: FavouritesDao
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SearchViewModel(theMovieDBApi, favouritesDao) as T
  }
}
