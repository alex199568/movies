package com.test.technical.movies.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Favourite::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
  abstract fun favouritesDao(): FavouritesDao
}
