package com.test.technical.movies

import com.test.technical.movies.model.MovieDetails
import com.test.technical.movies.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBApi {
  @GET("search/movie")
  fun search(@Query("query") query: String): Observable<SearchResponse>

  @GET("movie/{movieId}")
  fun details(@Path("movieId") movieId: Int): Observable<MovieDetails>
}
