package com.test.technical.movies.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.progressBar
import kotlinx.android.synthetic.main.fragment_search.searchResultsRecyclerView
import kotlinx.android.synthetic.main.fragment_search.searchView
import javax.inject.Inject

class SearchFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: SearchViewModel.Factory
  @Inject
  lateinit var favouritesDao: FavouritesDao

  private lateinit var viewModel: SearchViewModel

  private lateinit var adapter: SearchResultsAdapter

  private var latestPage = 0
  private var totalPages = 1

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_search, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
          adapter.clear()
          latestPage = 1
          totalPages = 1
          viewModel.search(it, latestPage)
          progressBar.visibility = View.VISIBLE
        }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        // TODO: show recent searches?
        return false
      }
    })

    adapter = SearchResultsAdapter(context!!)
    adapter.onItemLongClick {
      Observable
          .fromCallable { favouritesDao.insert(Favourite(it)) }
          .subscribeOn(Schedulers.io())
          .subscribe()
    }

    searchResultsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!searchResultsRecyclerView.canScrollVertically(1) && latestPage != totalPages) {
          viewModel.search(searchView.query.toString(), ++latestPage)
          progressBar.visibility = View.VISIBLE
        }
      }
    })
    searchResultsRecyclerView.layoutManager = LinearLayoutManager(context!!)
    searchResultsRecyclerView.adapter = adapter


    viewModel.searchResponse.observe(this, Observer {
      if (it != null) {
        totalPages = it.totalPages
        adapter.add(it.results)
        progressBar.visibility = View.GONE
      }
    })
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
  }

  companion object {
    @JvmStatic
    fun newInstance(): SearchFragment =
        SearchFragment()
  }
}
