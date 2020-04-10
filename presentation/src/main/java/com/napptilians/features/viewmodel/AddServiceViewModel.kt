package com.napptilians.features.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
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
) : BaseViewModel<AddServiceViewModel>() {

    private val _addServiceDataStream = MutableLiveData<UiStatus<Long, ErrorModel>>()
    val addServiceDataStream: LiveData<UiStatus<Long, ErrorModel>>
        get() = _addServiceDataStream

    val service = MutableLiveData<ServiceModel>(ServiceModel())
    val valid = MediatorLiveData<Boolean>()

    init {
        valid.addSource(service) {
            Log.d("Cacatua", "checking service validation")
            valid.value = isFormValid(it)
        }
    }

    private fun isFormValid(service: ServiceModel): Boolean =
        service.categoryId != null
                && service.name != null
                && service.day != null
                && service.spots != null
                && service.durationMin != null
                && service.description != null

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
                    1,
                    30,
                    "123123123oajsdoj"
                )
            )
            _addServiceDataStream.value = processModel(request)
        }
    }
}
