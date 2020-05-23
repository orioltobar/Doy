package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.AddAttendeeUseCase
import com.napptilians.domain.usecases.DeleteAttendeeUseCase
import com.napptilians.domain.usecases.GetUserUseCase
import com.napptilians.features.Error
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.base.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServiceDetailViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val addAttendeeUseCase: AddAttendeeUseCase,
    private val deleteAttendeeUseCase: DeleteAttendeeUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<ServiceModel>() {

    private val _addAttendeeServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val addAttendeeServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _addAttendeeServiceDataStream

    private val _deleteAttendeeServiceDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val deleteAttendeeServiceDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _deleteAttendeeServiceDataStream

    private val _userDataStream = SingleLiveEvent<UiStatus<ChatRequestModel, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<ChatRequestModel, ErrorModel>> get() = _userDataStream

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

    fun executeGetChatInformation(
        serviceId: Long,
        serviceName: String,
        serviceStartDate: ZonedDateTime
    ) {
        viewModelScope.launch {
            _userDataStream.setValue(emitLoadingState())
            val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
            currentUserRequest.either(
                onSuccess = { userModel ->
                    val requestModel = ChatRequestModel(
                        userModel.id,
                        serviceId,
                        userModel.name,
                        serviceName,
                        serviceStartDate
                    )
                    _userDataStream.setValue(NewValue(requestModel))
                },
                onFailure = {
                    _userDataStream.setValue(Error(ErrorModel("")))
                }
            )
        }
    }
}
