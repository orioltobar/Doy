package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.AddAttendeeUseCase
import com.napptilians.domain.usecases.DeleteAttendeeUseCase
import com.napptilians.domain.usecases.DeleteServiceUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServiceDetailViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val addAttendeeUseCase: AddAttendeeUseCase,
    private val deleteAttendeeUseCase: DeleteAttendeeUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase
) : BaseViewModel<ServiceModel>() {

    private val _addAttendeeServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val addAttendeeServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _addAttendeeServiceDataStream

    private val _deleteAttendeeServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val deleteAttendeeServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _deleteAttendeeServiceDataStream

    private val _deleteServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val deleteServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _deleteServiceDataStream


    fun executeAdd(serviceId: Long) {
        viewModelScope.launch {
            _addAttendeeServiceDataStream.value = emitLoadingState()
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val request = addAttendeeUseCase.execute(uid, serviceId)
            _addAttendeeServiceDataStream.value = processModel(request)
        }
    }

    fun executeDelete(serviceId: Long) {
        viewModelScope.launch {
            _deleteAttendeeServiceDataStream.value = emitLoadingState()
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val request = deleteAttendeeUseCase.execute(uid, serviceId)
            _deleteAttendeeServiceDataStream.value = processModel(request)
        }
    }

    fun executeDeleteService(serviceId: Long) {
        viewModelScope.launch {
            _deleteServiceDataStream.value = emitLoadingState()
            val request = deleteServiceUseCase.execute(serviceId)
            _deleteServiceDataStream.value = processModel(request)
        }
    }
}
