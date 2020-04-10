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

    private var service = ServiceModel()
    val serviceCategory = MutableLiveData("")
    val serviceName = MutableLiveData("")
    val serviceDay = MutableLiveData("")
    val serviceHour = MutableLiveData("")
    val serviceSpots = MutableLiveData("")
    val serviceDuration = MutableLiveData("")
    val serviceDescription = MutableLiveData<String>("")
    val isValidService = MediatorLiveData<Boolean>()

    init {
        isValidService.apply {
            addSource(serviceName) {
                service.name = serviceName.value
                Log.d(TAG, "serviceNameChanged: $serviceName")
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDay) {
                service.day = serviceDay.value
                Log.d(TAG, "serviceDayChanged: $serviceName")
                isValidService.value = it.isNotBlank()
            }
            addSource(serviceDescription) {
                service.description = serviceDescription.value
                Log.d(TAG, "serviceDescriptionChanged: $serviceName")
                isValidService.value = isFormValid(service)
            }
            // TODO: Add other sources :)
        }
    }

    private fun isFormValid(service: ServiceModel): Boolean =
        // !service.categoryId.isNullOrBlank() &&
        !service.name.isNullOrBlank()
                && !service.day.isNullOrBlank()
        //        && !service.spots.isNullOrBlank()
        //        && !service.durationMin.isNullOrBlank()
                && !service.description.isNullOrBlank()

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

    companion object {
        private val TAG = AddServiceViewModel::class.java.simpleName
    }
}
