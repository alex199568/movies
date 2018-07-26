package com.test.technical.movies.favourites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import com.test.technical.movies.data.FavouritesDao
import javax.inject.Inject

class FavouritesFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: FavouritesViewModel.Factory
  @Inject
  lateinit var favouritesDao: FavouritesDao

  private lateinit var viewModel: FavouritesViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_favourites, container, false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouritesViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    favouritesDao.getAll().observe(this, Observer {
      Log.d("zxcv", "Data changed")
      it?.forEach { Log.d("zxcv", it.toString()) }
    })
  }

  companion object {
    @JvmStatic
    fun newInstance(): FavouritesFragment =
        FavouritesFragment()
  }
}
