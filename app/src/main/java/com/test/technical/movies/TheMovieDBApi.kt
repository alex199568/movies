package com.test.technical.movies

import com.test.technical.movies.model.MovieDetails
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
//import retrofit2.http.Query

interface TheMovieDBApi {
//  @GET("search/movie")
//  fun search(@Query("query") query: String)

  @GET("movie/{movieId}")
  fun details(@Path("movieId") movieId: Int): Observable<MovieDetails>
}
