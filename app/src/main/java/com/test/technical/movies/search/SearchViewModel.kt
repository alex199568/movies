package com.test.technical.movies.search

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.model.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel(private val theMovieDBApi: TheMovieDBApi) : ViewModel() {
  val searchResponse = MutableLiveData<SearchResponse>()

  fun search(query: String) {
    theMovieDBApi
        .search(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { searchResponse.postValue(it) }
  }

  class Factory @Inject constructor(private val theMovieDBApi: TheMovieDBApi) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SearchViewModel(theMovieDBApi) as T
  }
}
