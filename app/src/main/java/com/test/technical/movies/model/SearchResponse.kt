package com.test.technical.movies.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    val results: List<SearchResult>
)
