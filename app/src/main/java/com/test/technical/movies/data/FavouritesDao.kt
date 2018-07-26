package com.test.technical.movies.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface FavouritesDao {
  @Query("SELECT * FROM favourites")
  fun getAll(): LiveData<List<Favourite>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(favourite: Favourite)

  @Delete
  fun delete(favourite: Favourite)
}
