package com.test.technical.movies.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.technical.movies.TheMovieDBApi
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.model.MovieDetails
import com.test.technical.movies.util.TestSchedulers
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.Date

@RunWith(JUnit4::class)
class MovieDetailsViewModelTest {
  @Mock
  private lateinit var favouritesDao: FavouritesDao
  @Mock
  private lateinit var theMovieDBApi: TheMovieDBApi

  @get:Rule
  val instantExecutor = InstantTaskExecutorRule()

  private lateinit var viewModel: MovieDetailsViewModel

  private val schedulers = TestSchedulers()

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    viewModel = MovieDetailsViewModel(theMovieDBApi, favouritesDao, schedulers)
  }

  @Test
  fun testAddToFavourites() {
    val details = createMovieDetails(9)
    val favourite = Favourite(details)

    viewModel.addToFavourites(details)

    verify(favouritesDao).insert(favourite)
  }

  @Test
  fun testRemoveFromFavourites() {
    val details = createMovieDetails(8)

    viewModel.removeFromFavourites(details)

    verify(favouritesDao).deleteByMovieDBId(8)
  }

  @Test
  fun testRequestMovieDetails() {
    val details = createMovieDetails(3)

    `when`(theMovieDBApi.details(3)).thenReturn(Observable.just(details))

    viewModel.movieDetails.observeForever { assertEquals(details, it) }

    viewModel.requestMovieDetails(3)
  }

  @Test
  fun testOnError() {
    val errorCallback: () -> Unit = { }
    val callbackSpy = spy(errorCallback)

    viewModel.onErrorCallback = callbackSpy

    `when`(theMovieDBApi.details(anyInt())).thenReturn(Observable.error(Exception()))

    viewModel.requestMovieDetails(3)

    verify(callbackSpy).invoke()
  }

  private fun createMovieDetails(id: Int): MovieDetails = MovieDetails(
      adult = false,
      backdropPath = "",
      collection = null,
      budget = 1,
      genres = listOf(),
      homepage = "",
      id = id,
      overview = "",
      popularity = 0.2,
      status = "",
      tagline = "",
      video = false,
      revenue = 2,
      runtime = 2,
      imdbId = "",
      originalTitle = "",
      posterPath = "",
      originalLanguage = "",
      productionCompanies = listOf(),
      productionCountries = listOf(),
      spokenLanguages = listOf(),
      releaseDate = Date(),
      voteAverage = 2.3,
      voteCount = 1
  )
}
