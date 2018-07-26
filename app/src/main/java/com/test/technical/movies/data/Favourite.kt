package com.test.technical.movies.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.test.technical.movies.model.SearchResult

@Entity(tableName = "favourites")
data class Favourite(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "themoviedb_id") val movieDbId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "poster_path") val posterPath: String
) {
  constructor(searchResult: SearchResult) : this(
      null,
      searchResult.id,
      searchResult.title,
      searchResult.posterPath
  )
}
