package com.test.technical.movies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.someText

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    val theMovieDBApi = (application as MoviesApp).appComponent.theMovieDBApi()

    theMovieDBApi.details(557)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          someText.text = it.toString()
        }

    theMovieDBApi.search("spider")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          Log.d("zxcv", "page ${it.page}")
          Log.d("zxcv", "total pages ${it.totalPages}")
          Log.d("zxcv", "total results ${it.totalResults}")
          it.results.forEach {
            Log.d("zxcv", it.toString())
          }
        }
  }
}
