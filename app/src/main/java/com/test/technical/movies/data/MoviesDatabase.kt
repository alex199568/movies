package com.test.technical.movies.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Favourite::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
  abstract fun favouritesDao(): FavouritesDao

  companion object {
    private var instance: MoviesDatabase? = null

    @JvmStatic
    fun getInstance(context: Context): MoviesDatabase? {
      if (instance == null) {
        synchronized(MoviesDatabase::class) {
          instance = Room.databaseBuilder(
              context.applicationContext,
              MoviesDatabase::class.java,
              "movies.db"
          ).build()
        }
      }
      return instance
    }

    fun free() {
      instance = null
    }
  }
}
