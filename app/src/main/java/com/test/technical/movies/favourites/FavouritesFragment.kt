package com.test.technical.movies.favourites

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import javax.inject.Inject

class FavouritesFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: FavouritesViewModel.Factory

  private lateinit var viewModel: FavouritesViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_favourites, container, false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouritesViewModel::class.java)
  }

  companion object {
    @JvmStatic
    fun newInstance(): FavouritesFragment =
        FavouritesFragment()
  }
}
