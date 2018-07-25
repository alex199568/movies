package com.test.technical.movies.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import com.test.technical.movies.MoviesApp
import com.test.technical.movies.R
import kotlinx.android.synthetic.main.fragment_search.searchResultsRecyclerView
import kotlinx.android.synthetic.main.fragment_search.searchView
import javax.inject.Inject

class SearchFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: SearchViewModel.Factory

  private lateinit var viewModel: SearchViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_search, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    searchView.setOnQueryTextListener(object: OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.search(it) }
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        // TODO: show recent searches?
        return false
      }
    })

    viewModel.searchResponse.observe(this, Observer {
      val context = context
      if (it != null && context != null) {
        val searchResultsAdapter = SearchResultsAdapter(it.results, context)
        searchResultsRecyclerView.apply {
          layoutManager = LinearLayoutManager(context)
          adapter = searchResultsAdapter
        }
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
