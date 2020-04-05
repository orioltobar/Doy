//package com.orioltobar.androidklean.view.movielist
//
//import android.R
//import android.widget.FrameLayout
//import androidx.test.rule.ActivityTestRule
//import com.orioltobar.androidklean.UiAssertions
//import com.orioltobar.androidklean.base.MockActivity
//import com.orioltobar.domain.models.movie.MovieGenreDetailModel
//import com.orioltobar.domain.models.movie.MovieGenresModel
//import com.orioltobar.domain.models.movie.MovieModel
//import io.mockk.MockKAnnotations
//import io.mockk.every
//import io.mockk.impl.annotations.MockK
//import kotlinx.android.synthetic.main.movie_list_item.view.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
//class MovieListViewHolderTest : UiAssertions {
//
//    init {
//        MockKAnnotations.init(this, relaxed = true)
//    }
//
//    private lateinit var holder: MovieListViewHolder
//
//    @MockK
//    private lateinit var movieModel: MovieModel
//
//    @MockK
//    private lateinit var genresModel: MovieGenresModel
//
//    @get:Rule
//    val activityRule =
//        object : ActivityTestRule<MockActivity>(MockActivity::class.java, false, false) {
//            override fun afterActivityLaunched() {
//                super.afterActivityLaunched()
//                runOnUiThread {
//                    holder = MovieListViewHolder(activity.findViewById<FrameLayout>(R.id.content))
//                    activity.setView(holder.itemView)
//                }
//            }
//        }
//
//    @Before
//    fun setup() {
//        activityRule.launchActivity(null)
//
//        every { genresModel.genre } returns listOf(MovieGenreDetailModel(12, "Action"))
//        every { movieModel.title } returns "Title test"
//        every { movieModel.voteAverage } returns 5.0F
//        every { movieModel.originalLanguage } returns "English"
//        every { movieModel.genres } returns genresModel
//        every { movieModel.releaseDate } returns "Jan 1979"
//        every { movieModel.frontImageUrl } returns "https://image.tmdb.org/t/p/w300/6sjMsBcIuqU44GpG5tL33KUFOQR.jpg"
//    }
//
//    @Test
//    fun setViewHolderInformation() {
//        val expectedRate = activityRule.activity.resources.getString(
//            com.orioltobar.androidklean.R.string.rate,
//            movieModel.voteAverage
//        )
//
//        activityRule.runOnUiThread {
//            holder.update(movieModel)
//        }
//
//        Thread.sleep(500)
//
//        checkTextIsDisplayed(movieModel.title)
//        checkTextIsDisplayed(genresModel.genre[0].name)
//        checkTextIsDisplayed(expectedRate)
//        checkTextIsDisplayed(movieModel.releaseDate)
//        checkViewIsDisplayed(holder.itemView.movieViewHolderCover.id)
//    }
//}
