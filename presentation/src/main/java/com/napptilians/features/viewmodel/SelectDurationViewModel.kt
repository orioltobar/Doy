package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectDurationViewModel @Inject constructor() : BaseViewModel<MovieModel>() {

    private val _durationsDataStream = MutableLiveData<UiStatus<List<Int>, ErrorModel>>()
    val durationsDataStream: LiveData<UiStatus<List<Int>, ErrorModel>>
        get() = _durationsDataStream

    fun execute() {
        viewModelScope.launch {
            _durationsDataStream.value = emitLoadingState()
            val durationList = (MIN_HOURS..MAX_HOURS).toList()
            _durationsDataStream.value = processModel(Success(durationList))
        }
    }

    companion object {
        private const val MIN_HOURS = 1
        private const val MAX_HOURS = 5
    }
}
