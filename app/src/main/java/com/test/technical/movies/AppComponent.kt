package com.test.technical.movies

import com.test.technical.movies.details.MovieDetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
  fun theMovieDBApi(): TheMovieDBApi

  fun inject(fragment: MovieDetailsFragment)
}