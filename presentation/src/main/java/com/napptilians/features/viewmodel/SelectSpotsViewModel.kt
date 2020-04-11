package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.models.service.SpotsModel
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectSpotsViewModel @Inject constructor() : BaseViewModel<MovieModel>() {

    private val _spotsDataStream = MutableLiveData<UiStatus<List<SpotsModel>, ErrorModel>>()
    val spotsDataStream: LiveData<UiStatus<List<SpotsModel>, ErrorModel>>
        get() = _spotsDataStream

    fun execute() {
        viewModelScope.launch {
            _spotsDataStream.value = emitLoadingState()
            val spotList = (MIN_PERSONS..MAX_PERSONS).toList().map {
                SpotsModel(
                    it
                )
            }
            _spotsDataStream.value = processModel(Success(spotList))
        }
    }

    companion object {
        private const val MIN_PERSONS = 1
        private const val MAX_PERSONS = 10
    }
}
