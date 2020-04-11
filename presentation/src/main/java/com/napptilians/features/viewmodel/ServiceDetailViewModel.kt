package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.AddAttendeeUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServiceDetailViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val addAttendeeUseCase: AddAttendeeUseCase
) : BaseViewModel<ServiceModel>() {

    private val _addAttendeeServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val addAttendeeServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _addAttendeeServiceDataStream

    fun execute(serviceId: Long) {
        viewModelScope.launch {
            _addAttendeeServiceDataStream.value = emitLoadingState()
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val request = addAttendeeUseCase.execute(uid, serviceId)
            _addAttendeeServiceDataStream.value = processModel(request)
        }
    }
}
