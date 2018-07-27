package com.test.technical.movies.favourites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import kotlinx.android.synthetic.main.fragment_favourites.favouritesRecyclerView
import kotlinx.android.synthetic.main.fragment_favourites.noFavouritesMessage
import javax.inject.Inject

class FavouritesFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: FavouritesViewModel.Factory

  private lateinit var viewModel: FavouritesViewModel

  private lateinit var adapter: FavouritesAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_favourites, container, false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouritesViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = FavouritesAdapter(context!!).apply {
      onRemoveFromFavourites { viewModel.remove(it) }
    }

    favouritesRecyclerView.layoutManager = LinearLayoutManager(context!!)
    favouritesRecyclerView.adapter = adapter

    viewModel.all().observe(this, Observer {
      if (it == null || it.isEmpty()) {
        noFavouritesMessage.visibility = View.VISIBLE
        favouritesRecyclerView.visibility = View.GONE
      } else {
        noFavouritesMessage.visibility = View.GONE
        favouritesRecyclerView.visibility = View.VISIBLE
        adapter.favourites = it
      }
    })
  }

  companion object {
    @JvmStatic
    fun newInstance(): FavouritesFragment =
        FavouritesFragment()
  }
}
