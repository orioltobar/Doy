package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.domain.usecases.AddServiceUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddServiceViewModel @Inject constructor(
    private val addServiceUseCase: AddServiceUseCase
) : BaseViewModel<MovieModel>() {

    private val _addServiceDataStream = MutableLiveData<UiStatus<Long, ErrorModel>>()
    val addServiceDataStream: LiveData<UiStatus<Long, ErrorModel>>
        get() = _addServiceDataStream

    fun execute() {
        viewModelScope.launch {
            _addServiceDataStream.value = emitLoadingState()
            // TODO: Perform a request with real data
            val request = addServiceUseCase.execute(
                ServiceModel(
                    1,
                    1,
                    "prueba name",
                    "prueba desc",
                    ByteArray(10),
                    "2020-04-09",
                    null,
                    1,
                    30,
                    "123123123oajsdoj"
                )
            )
            _addServiceDataStream.value = processModel(request)
        }
    }
}
