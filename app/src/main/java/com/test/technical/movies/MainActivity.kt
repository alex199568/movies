package com.test.technical.movies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.test.technical.movies.details.MovieDetailsFragment

private const val DETAILS_FRAGMENT_TAG = "DetailsFragment"

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val detailsFragment = MovieDetailsFragment.newInstance(557)

    supportFragmentManager
        .beginTransaction()
        .add(R.id.screenContainer, detailsFragment, DETAILS_FRAGMENT_TAG)
        .commit()
  }
}
