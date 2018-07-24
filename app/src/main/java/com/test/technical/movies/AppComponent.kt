package com.test.technical.movies

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
  fun theMovieDBApi(): TheMovieDBApi
}