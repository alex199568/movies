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
import android.widget.Toast
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search.progressBar
import kotlinx.android.synthetic.main.fragment_search.searchResultsRecyclerView
import kotlinx.android.synthetic.main.fragment_search.searchView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val REQUEST_REPEAT_DELAY = 5L

class SearchFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: SearchViewModel.Factory

  private lateinit var viewModel: SearchViewModel

  private lateinit var searchResultsAdapter: SearchResultsAdapter

  private var latestPage = 0
  private var totalPages = 1

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_search, container, false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity?.let { (it.application as MoviesApp).appComponent.inject(this) }

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initSearchView()

    initAdapter()

    initViewModelOnError()

    initRecyclerView()

    observeSearchResponse()
    observeFavourites()
  }

  private fun initSearchView() {
    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
          searchResultsAdapter.clear()
          latestPage = 1
          totalPages = 1
          viewModel.search(it, latestPage)
          progressBar.visibility = View.VISIBLE
        }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        return false
      }
    })
  }

  private fun initAdapter() {
    searchResultsAdapter = SearchResultsAdapter(context!!).apply {
      addToFavouritesCallback = { viewModel.addToFavourites(it) }
      removeFromFavouritesCallback = { viewModel.removeFromFavourites(it) }
    }
  }

  private fun initViewModelOnError() {
    viewModel.onErrorCallback = {
      context?.let {
        Toast.makeText(it, it.getString(R.string.offlineMessage), Toast.LENGTH_SHORT).show()
      }
      Observable.timer(REQUEST_REPEAT_DELAY, TimeUnit.SECONDS).subscribe {
        val query = searchView.query
        if (query.isNotEmpty()) {
          viewModel.search(query.toString(), latestPage)
        }
      }
    }
  }

  private fun initRecyclerView() {
    searchResultsRecyclerView.apply {
      searchResultsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
          super.onScrollStateChanged(recyclerView, newState)
          if (!searchResultsRecyclerView.canScrollVertically(1) && latestPage != totalPages) {
            viewModel.search(searchView.query.toString(), ++latestPage)
            progressBar.visibility = View.VISIBLE
          }
        }
      })
      layoutManager = LinearLayoutManager(context!!)
      adapter = searchResultsAdapter
    }
  }

  private fun observeSearchResponse() {
    viewModel.searchResponse.observe(this, Observer {
      if (it != null) {
        totalPages = it.totalPages
        searchResultsAdapter.add(it.results)
        progressBar.visibility = View.GONE
      }
    })
  }

  private fun observeFavourites() {
    viewModel.favourites().observe(this, Observer { it?.let { searchResultsAdapter.updateFavourites(it) } })
  }
  companion object {
    @JvmStatic
    fun newInstance(): SearchFragment =
        SearchFragment()
  }
}
