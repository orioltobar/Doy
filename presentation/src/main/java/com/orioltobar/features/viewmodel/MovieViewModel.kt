package com.orioltobar.features.viewmodel

import androidx.lifecycle.*
import com.orioltobar.commons.Response
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.domain.models.movie.MovieModel
import com.orioltobar.domain.usecases.GetMovieUseCase
import com.orioltobar.domain.usecases.GetMovieUseCaseFlow
import com.orioltobar.features.UiStatus
import com.orioltobar.features.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MovieViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getMovieUseCaseFlow: GetMovieUseCaseFlow
) :
    BaseViewModel<MovieModel>() {

    private val _movieDataStream = MutableLiveData<UiStatus<MovieModel, ErrorModel>>()
    val movieDataStream: LiveData<UiStatus<MovieModel, ErrorModel>>
        get() = _movieDataStream

    private val _movieUiModelFlow =
        getMovieUseCaseFlow.execute().flowOn(Dispatchers.IO)
            .asLiveData(viewModelScope.coroutineContext)
    val movieModelFlow: LiveData<Response<MovieModel, ErrorModel>>
        get() = _movieUiModelFlow

    fun execute(id: Long) {
        viewModelScope.launch {
            _movieDataStream.value = emitLoadingState()
            val request = getMovieUseCase.execute(id)
            _movieDataStream.value = processModel(request)
        }
    }
}