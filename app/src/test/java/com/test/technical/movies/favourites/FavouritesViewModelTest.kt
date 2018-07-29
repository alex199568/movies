package com.test.technical.movies.favourites

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.test.technical.movies.data.Favourite
import com.test.technical.movies.data.FavouritesDao
import com.test.technical.movies.util.TestSchedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class FavouritesViewModelTest {
  @Mock
  private lateinit var favouritesDao: FavouritesDao

  @get:Rule
  val instantExecutor = InstantTaskExecutorRule()

  private lateinit var viewModel: FavouritesViewModel

  private val schedulers = TestSchedulers()

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    viewModel = FavouritesViewModel(favouritesDao, schedulers)
  }

  @Test
  fun testAllReturnsAllFavourites() {
    val favourites = listOf(
        Favourite(null, 1, "Movie 1", "image1"),
        Favourite(null, 2, "Movie 2", "image2")
    )
    val liveFavourites = MutableLiveData<List<Favourite>>()

    `when`(favouritesDao.getAll()).thenReturn(liveFavourites)

    viewModel.all().observeForever {
      assertEquals(favourites, it)
    }

    liveFavourites.postValue(favourites)
  }

  @Test
  fun testRemoveDeletesMovieWithMovieDBId() {
    val favourite = Favourite(null, 3, "Title", "poster")

    viewModel.remove(favourite)

    verify(favouritesDao).deleteByMovieDBId(3)
  }
}
