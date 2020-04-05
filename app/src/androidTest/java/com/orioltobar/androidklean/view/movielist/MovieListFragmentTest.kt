//package com.orioltobar.androidklean.view.movielist
//
//import android.os.Bundle
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.navigation.NavHostController
//import androidx.navigation.Navigation
//import androidx.navigation.testing.TestNavHostController
//import androidx.test.rule.ActivityTestRule
//import com.orioltobar.androidklean.R
//import com.orioltobar.androidklean.UiAssertions
//import com.orioltobar.androidklean.base.MockActivity
//import com.orioltobar.androidklean.di.TestViewModelModule
//import com.orioltobar.commons.error.ErrorModel
//import com.orioltobar.domain.models.movie.MovieGenreDetailModel
//import com.orioltobar.domain.models.movie.MovieGenresModel
//import com.orioltobar.domain.models.movie.MovieModel
//import com.orioltobar.features.NewValue
//import com.orioltobar.features.UiStatus
//import com.orioltobar.features.viewmodel.MovieListViewModel
//import io.mockk.MockKAnnotations
//import io.mockk.every
//import io.mockk.impl.annotations.MockK
//import io.mockk.mockk
//import kotlinx.android.synthetic.main.movie_list_fragment.*
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//@ExperimentalCoroutinesApi
//class MovieListFragmentTest : UiAssertions {
//
//    init {
//        MockKAnnotations.init(this, relaxed = true)
//    }
//
//    private lateinit var movieListFragment: MovieListFragment
//
//    @MockK
//    private lateinit var movieListViewModelMock: MovieListViewModel
//
//    private val _movieListDataStream = MutableLiveData<UiStatus<List<MovieModel>, ErrorModel>>()
//    private val movieListDataStream: LiveData<UiStatus<List<MovieModel>, ErrorModel>>
//        get() = _movieListDataStream
//
//    private lateinit var navHostController: NavHostController
//
//    @get:Rule
//    val rule = InstantTaskExecutorRule()
//
//    @get:Rule
//    val activityRule =
//        object : ActivityTestRule<MockActivity>(MockActivity::class.java, false, false) {
//            override fun afterActivityLaunched() {
//                super.afterActivityLaunched()
//                movieListFragment = MovieListFragment().also { fragment ->
//                    val args = Bundle().apply { putInt("id", 12) }
//                    fragment.arguments = args
//                }
//                activity.setFragment(movieListFragment)
//            }
//        }
//
//    @Before
//    fun setup() {
//        every { movieListViewModelMock.movieListDataStream } returns movieListDataStream
//        every { TestViewModelModule.viewModelFactory.create<MovieListViewModel>(any()) } returns movieListViewModelMock
//
//        activityRule.launchActivity(null)
//
//        navHostController = TestNavHostController(activityRule.activity)
//        navHostController.setGraph(R.navigation.nav_graph)
//
//        movieListFragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
//            if (viewLifecycleOwner != null) {
//                // The fragment's view has just been created
//                Navigation.setViewNavController(movieListFragment.requireView(), navHostController)
//            }
//        }
//    }
//
//    @Test
//    fun setMovieListTest() {
//        val expectedList = (getMovieListMock() as NewValue).result
//
//        activityRule.activity.runOnUiThread {
//            _movieListDataStream.value = getMovieListMock()
//        }
//
//        Thread.sleep(500)
//
//        assertTrue(movieListDataStream.value is NewValue)
//        checkViewIsNotDisplayed(movieListFragment.movieListProgressBar.id)
//        checkRecyclerViewItemCount(movieListFragment.movieListRecyclerView.id, expectedList.size)
//        expectedList.forEachIndexed { index, element ->
//            checkThatRecyclerViewItemHasText(
//                movieListFragment.movieListRecyclerView.id,
//                index,
//                element.title
//            )
//        }
//    }
//
//    @Test
//    fun onLoadingMustShowLoadingProgressBarTest() {
//        activityRule.activity.runOnUiThread {
//            movieListFragment.onLoading()
//        }
//
//        Thread.sleep(500)
//
//        checkViewIsDisplayed(movieListFragment.movieListProgressBar.id)
//    }
//
//    private fun getMovieListMock(): UiStatus<List<MovieModel>, ErrorModel> {
//        val genresModel = mockk<MovieGenresModel>()
//        val movieModel = mockk<MovieModel>()
//        every { genresModel.genre } returns listOf(MovieGenreDetailModel(12, "Action"))
//        every { movieModel.id } returns 12345L
//        every { movieModel.title } returns "Title test"
//        every { movieModel.voteAverage } returns 5.0F
//        every { movieModel.originalLanguage } returns "English"
//        every { movieModel.genres } returns genresModel
//        every { movieModel.releaseDate } returns "Jan 1979"
//        every { movieModel.frontImageUrl } returns "https://image.tmdb.org/t/p/w300/6sjMsBcIuqU44GpG5tL33KUFOQR.jpg"
//
//        return NewValue(listOf(movieModel, movieModel, movieModel, movieModel))
//    }
//}