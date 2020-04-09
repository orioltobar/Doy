package com.napptilians.features.viewmodel

import com.napptilians.commons.AppDispatchers
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.usecases.GetMovieUseCase
import com.napptilians.domain.usecases.GetMovieUseCaseFlow
import com.napptilians.features.base.BaseViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MovieViewModel @Inject constructor(
    appDispatchers: AppDispatchers,
    private val getMovieUseCase: GetMovieUseCase,
    private val getMovieUseCaseFlow: GetMovieUseCaseFlow
) :
    BaseViewModel<MovieModel>() {

//    private val ioDispatcher = appDispatchers.io
//
//    private val _movieDataStream = MutableLiveData<UiStatus<MovieModel, ErrorModel>>()
//    val movieDataStream: LiveData<UiStatus<MovieModel, ErrorModel>>
//        get() = _movieDataStream
//
//    private val _movieUiModelFlow =
//        getMovieUseCaseFlow.execute().flowOn(ioDispatcher)
//            .asLiveData(viewModelScope.coroutineContext)
//    val movieModelFlow: LiveData<Response<MovieModel, ErrorModel>>
//        get() = _movieUiModelFlow
//
//    fun execute(id: Long) {
//        viewModelScope.launch {
//            _movieDataStream.value = emitLoadingState()
//            val request = getMovieUseCase.execute(id)
//            _movieDataStream.value = processModel(request)
//        }
//    }
}
