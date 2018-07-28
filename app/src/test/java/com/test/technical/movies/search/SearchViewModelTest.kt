package com.test.technical.movies.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.model.SearchResponse
import com.test.technical.movies.model.SearchResult
import com.test.technical.movies.util.TestSchedulers
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SearchViewModelTest {
  @Mock
  private lateinit var favouritesDao: FavouritesDao
  @Mock
  private lateinit var theMovieDBApi: TheMovieDBApi

  @get:Rule
  val instantExecutor = InstantTaskExecutorRule()

  private lateinit var viewModel: SearchViewModel

  private val schedulers = TestSchedulers()

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    viewModel = SearchViewModel(theMovieDBApi, favouritesDao, schedulers)
  }

  @Test
  fun testSearch() {
    val results = listOf(
        createSearchResult(1, "Title1"),
        createSearchResult(2, "Title2")
    )
    val response = SearchResponse(1, 2, 1, results)

    val query = "Some movie"
    val page = 2

    `when`(theMovieDBApi.search(query, page)).thenReturn(Observable.just(response))

    viewModel.searchResponse.observeForever { assertEquals(response, it) }

    viewModel.search(query, page)
  }

  @Test
  fun testFavourites() {
    val favourites = listOf(
        Favourite(null, 1, "Movie 1", "image1"),
        Favourite(null, 2, "Movie 2", "image2")
    )
    val liveFavourites = MutableLiveData<List<Favourite>>()

    `when`(favouritesDao.getAll()).thenReturn(liveFavourites)

    viewModel.favourites().observeForever {
      assertEquals(favourites, it)
    }

    liveFavourites.postValue(favourites)
  }

  @Test
  fun testRemoveFromFavourites() {
    val result = createSearchResult(3, "title")

    viewModel.removeFromFavourites(result)

    verify(favouritesDao).deleteByMovieDBId(3)
  }

  @Test
  fun testAddToFavourites() {
    val result = createSearchResult(3, "title")

    viewModel.addToFavourites(result)

    verify(favouritesDao).insert(Favourite(null, 3, "title", "back1"))
  }

  private fun createSearchResult(id: Int, title: String) =
      SearchResult(
          voteCount = 1,
          voteAverage = 1.0,
          id = id,
          video = false,
          title = title,
          popularity = 0.5,
          originalLanguage = "en",
          originalTitle = "Original $title",
          genreIds = listOf(),
          backdropPath = "back1",
          adult = false,
          overview = "overview",
          releaseDate = "15/10/2018",
          posterPath = "poster"
      )
}
