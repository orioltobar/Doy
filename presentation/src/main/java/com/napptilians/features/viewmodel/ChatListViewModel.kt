package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetServicesUseCase
import com.napptilians.domain.usecases.GetUserUseCase
import com.napptilians.features.Error
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.base.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val servicesUseCase: GetServicesUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<ServiceModel>() {

    private val _chatListDataStream = MutableLiveData<UiStatus<List<ServiceModel>, ErrorModel>>()
    val chatListDataStream: LiveData<UiStatus<List<ServiceModel>, ErrorModel>> get() = _chatListDataStream

    private val _userDataStream = SingleLiveEvent<UiStatus<ChatRequestModel, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<ChatRequestModel, ErrorModel>> get() = _userDataStream

    init {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                _chatListDataStream.value = emitLoadingState()
                val request = servicesUseCase.execute(emptyList(), null, it.uid)
                _chatListDataStream.value = processModel(request)
            }
        }
    }

    fun getChatInformation(serviceId: Long, serviceName: String) {
        viewModelScope.launch {
            _userDataStream.setValue(emitLoadingState())
            val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
            currentUserRequest.either(
                onSuccess = { userModel ->
                    val requestModel = ChatRequestModel(
                        userModel.id,
                        serviceId,
                        userModel.name,
                        serviceName
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