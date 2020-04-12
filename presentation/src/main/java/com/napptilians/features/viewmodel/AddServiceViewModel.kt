package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.AddServiceUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddServiceViewModel @Inject constructor(
    private val addServiceUseCase: AddServiceUseCase,
    private val firebaseAuth: FirebaseAuth
) : BaseViewModel<AddServiceViewModel>() {

    private val _addServiceDataStream = MutableLiveData<UiStatus<Long, ErrorModel>>()
    val addServiceDataStream: LiveData<UiStatus<Long, ErrorModel>>
        get() = _addServiceDataStream

    var service = ServiceModel()
    val serviceCategory = MutableLiveData("")
    val serviceName = MutableLiveData("")
    val serviceDay = MutableLiveData("")
    val serviceDate = MutableLiveData("")
    val serviceSpots = MutableLiveData("")
    val serviceDuration = MutableLiveData("")
    val serviceDescription = MutableLiveData("")
    val isValidService = MediatorLiveData<Boolean>()

    init {
        isValidService.apply {
            addSource(serviceCategory) {
                isValidService.value = isFormValid(service)
            }
            addSource(serviceName) {
                service.name = serviceName.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDay) {
                service.day = serviceDay.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDate) {
                service.hour = serviceDate.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDescription) {
                service.description = serviceDescription.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceSpots) {
                service.spots = serviceSpots.value?.toIntOrNull()
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDuration) {
                service.durationMin = serviceDuration.value
                    ?.substringBefore(" ")
                    ?.toIntOrNull()
                    ?.times(60) // hours to mins
                isValidService.value = isFormValid(service)
            }
        }
    }

    private fun isFormValid(service: ServiceModel): Boolean =
        service.categoryId != null
                && service.spots != null
                && service.durationMin != null
                && !service.name.isNullOrBlank()
                && !service.day.isNullOrBlank()
                && !service.description.isNullOrBlank()

    fun execute() {
        viewModelScope.launch {
            _addServiceDataStream.value = emitLoadingState()
            service.ownerId = firebaseAuth.currentUser?.uid ?: ""
            val request = addServiceUseCase.execute(service)
            _addServiceDataStream.value = processModel(request)
        }
    }
}
