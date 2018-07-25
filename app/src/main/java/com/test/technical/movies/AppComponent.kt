package com.test.technical.movies

import com.test.technical.movies.details.MovieDetailsFragment
import com.test.technical.movies.favourites.FavouritesFragment
import com.test.technical.movies.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
  fun theMovieDBApi(): TheMovieDBApi

  fun inject(fragment: MovieDetailsFragment)
  fun inject(fragment: SearchFragment)
  fun inject(fragment: FavouritesFragment)
}