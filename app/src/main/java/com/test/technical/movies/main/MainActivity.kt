package com.test.technical.movies.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.test.technical.movies.R.layout
import com.test.technical.movies.favourites.FavouritesFragment
import com.test.technical.movies.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.viewPager

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    viewPager.adapter = MainAdapter(
        listOf(
            FavouritesFragment.newInstance(),
            SearchFragment.newInstance()
        ),
        listOf(
            "Favourites",
            "Search"
        ),
        supportFragmentManager
    )
  }
}
