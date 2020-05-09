package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
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

    private val _userDataStream = SingleLiveEvent<UiStatus<List<Pair<Long, String>>, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<List<Pair<Long, String>>, ErrorModel>> get() = _userDataStream

    init {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                _chatListDataStream.value = emitLoadingState()
                val request = servicesUseCase.execute(emptyList(), null, it.uid)
                _chatListDataStream.value = processModel(request)
            }
        }
    }

    fun getTargetUser(serviceId: Long, serviceOwnerUid: String) {
        viewModelScope.launch {
            _userDataStream.setValue(emitLoadingState())
            val targetUserRequest = getUserUseCase(serviceOwnerUid)
            val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
            if (targetUserRequest is Success && currentUserRequest is Success) {
                val senderInfo = Pair(currentUserRequest.result.id, currentUserRequest.result.name)
                val serviceInfo = Pair(serviceId, targetUserRequest.result.id.toString())
                _userDataStream.setValue(NewValue(listOf(senderInfo, serviceInfo)))
            } else {
                _userDataStream.setValue(Error(ErrorModel("")))
            }
        }
    }
}