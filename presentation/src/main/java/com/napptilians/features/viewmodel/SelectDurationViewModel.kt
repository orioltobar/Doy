package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.models.service.DurationModel
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectDurationViewModel @Inject constructor() : BaseViewModel<MovieModel>() {

    private val _durationsDataStream = MutableLiveData<UiStatus<List<DurationModel>, ErrorModel>>()
    val durationsDataStream: LiveData<UiStatus<List<DurationModel>, ErrorModel>>
        get() = _durationsDataStream

    fun execute() {
        viewModelScope.launch {
            _durationsDataStream.value = emitLoadingState()
            val durationList = (MIN_MINUTES..MAX_MINUTES step STEP_MINUTES).toList().map {
                DurationModel(
                    it
                )
            }
            _durationsDataStream.value = processModel(Success(durationList))
        }
    }

    companion object {
        private const val MIN_MINUTES = 15
        private const val STEP_MINUTES = 15
        private const val MAX_MINUTES = 180
    }
}
