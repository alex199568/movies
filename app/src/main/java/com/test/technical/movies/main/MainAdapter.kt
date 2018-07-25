package com.test.technical.movies.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainAdapter(
    private val fragments: List<Fragment>,
    private val titles: List<String>,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
  override fun getItem(position: Int): Fragment = fragments[position]

  override fun getCount(): Int = fragments.size

  override fun getPageTitle(position: Int): CharSequence? = titles[position]
}
