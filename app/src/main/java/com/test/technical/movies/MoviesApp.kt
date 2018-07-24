package com.test.technical.movies

import android.app.Application

class MoviesApp : Application() {
  lateinit var appComponent: AppComponent

  override fun onCreate() {
    super.onCreate()

    appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
  }
}
