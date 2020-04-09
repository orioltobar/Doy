// package com.napptilians.doy.view.movie
//
// import android.graphics.drawable.Drawable
// import android.os.Bundle
// import android.view.LayoutInflater
// import android.view.View
// import android.view.ViewGroup
// import androidx.fragment.app.viebuiwModels
// import androidx.lifecycle.Observer
// import androidx.navigation.fragment.navArgs
// import com.bumptech.glide.GenericTransitionOptions
// import com.bumptech.glide.Glide
// import com.bumptech.glide.load.DataSource
// import com.bumptech.glide.load.engine.GlideException
// import com.bumptech.glide.request.RequestListener
// import com.bumptech.glide.request.target.Target
// import com.google.android.material.appbar.AppBarLayout
// import com.napptilians.doy.R
// import com.napptilians.doy.base.BaseFragment
// import com.napptilians.doy.extensions.getDominantColor
// import com.napptilians.commons.error.ErrorModel
// import com.napptilians.domain.models.movie.MovieModel
// import com.napptilians.features.UiStatus
// import com.napptilians.features.viewmodel.MovieViewModel
// import kotlinx.android.synthetic.main.movie_fragment.*
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlin.math.abs
//
// @ExperimentalCoroutinesApi
// class MovieFragment : BaseFragment() {
//
//    private val args: MovieFragmentArgs by navArgs()
//
//    private val viewModel: MovieViewModel by viewModels { vmFactory }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? = inflater.inflate(R.layout.movie_fragment, container, false)
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initAppBarListener()
//
//        viewModel.execute(args.id)
//        viewModel.movieDataStream.observe(
//            viewLifecycleOwner,
//            Observer<UiStatus<MovieModel, ErrorModel>> { handleUiStates(it, ::processNewValue) })
//    }
//
//    private fun processNewValue(model: MovieModel) {
//        movieFragmentTitle.text = model.title
//
//        movieFragmentReleaseDate.text = model.releaseDate
//        movieFragmentOverview.text = model.overview
//
//        context?.let {
//            movieFragmentRate.text =
//                it.resources.getString(R.string.rate, model.voteAverage.toString())
//            // Image
//            Glide.with(it)
//                .load(model.frontImageUrl)
//                .transition(GenericTransitionOptions.with(R.anim.fade_in))
//                .listener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?, model: Any?,
//                        target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?, model: Any?,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        resource?.let {
//                            movieFragmentBackground.setBackgroundColor(resource.getDominantColor())
//                            movieFragmentBackground.background.alpha = 80
//                        }
//                        return false
//                    }
//                })
//                .into(movieFragmentImage)
//        }
//
//        progressBar.visibility = View.GONE
//        movieFragmentSwipeAnimation.visibility = View.VISIBLE
//    }
//
//    override fun onError(error: ErrorModel) {
//        println("TRACK STATUS: ERROR!")
//    }
//
//    override fun onLoading() {
//        progressBar.visibility = View.VISIBLE
//        println("TRACK STATUS: LOADING...")
//    }
//
//    private fun initAppBarListener() {
//        movieFragmentAppbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
//            if (abs(verticalOffset) > 0) {
//                movieFragmentSwipeAnimation.visibility = View.GONE
//            } else {
//                movieFragmentSwipeAnimation.visibility = View.VISIBLE
//            }
//        })
//    }
// }
