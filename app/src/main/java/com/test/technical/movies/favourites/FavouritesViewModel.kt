package com.test.technical.movies.favourites

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class FavouritesViewModel : ViewModel() {
  class Factory @Inject constructor() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        FavouritesViewModel() as T
  }
}
