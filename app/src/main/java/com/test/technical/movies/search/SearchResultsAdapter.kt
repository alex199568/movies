package com.test.technical.movies.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.technical.movies.R
import com.test.technical.movies.search.SearchResultsAdapter.SearchResultViewHolder

import com.test.technical.movies.model.SearchResult

class SearchResultsAdapter(
    private val results: List<SearchResult>,
    private val context: Context
) : RecyclerView.Adapter<SearchResultViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
    val itemView = LayoutInflater.from(context).inflate(R.layout.search_result, parent, false)
    return SearchResultViewHolder(itemView)
  }

  override fun getItemCount(): Int = results.size

  override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
    holder.bind(results[position])
  }

  inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(result: SearchResult) {
      itemView.findViewById<TextView>(R.id.title).text = result.title
    }
  }
}
