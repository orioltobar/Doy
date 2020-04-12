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
import kotlinx.coroutines.launch

import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val servicesUseCase: GetServicesUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<ServiceModel>() {

    private val _chatListDataStream = MutableLiveData<UiStatus<List<ServiceModel>, ErrorModel>>()
    val chatListDataStream: LiveData<UiStatus<List<ServiceModel>, ErrorModel>> get() = _chatListDataStream

    // TODO: Fix userDataStream
    private val _userDataStream = MutableLiveData<UiStatus<Pair<Long, Long>, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<Pair<Long, Long>, ErrorModel>> get() = _userDataStream

    init {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                _chatListDataStream.value = emitLoadingState()
                val request = servicesUseCase.execute(emptyList(), null, it.uid)
                _chatListDataStream.value = processModel(request)
            }
        }
    }

    // TODO: Check how to cancel observer after view is destroyed, to avoid re send the
    // TODO: last value of live data after fragment is recreated.
//    fun getTargetUser(uid: String) {
//        viewModelScope.launch {
//            //            _userDataStream.value = emitLoadingState()
//            val targetUserRequest = getUserUseCase(uid)
//            val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
//            if (targetUserRequest is Success && currentUserRequest is Success) {
//                val idPair = Pair(currentUserRequest.result.id, targetUserRequest.result.id)
//                _userDataStream.value = NewValue(idPair)
//            } else {
//                _userDataStream.value = Error(ErrorModel(""))
//            }
//        }
//    }

    // TODO: Remove after MVP.
    suspend fun retrieveChatParameters(serviceId: Long, serviceOwnerUid: String): UiStatus<List<Pair<Long, String>>, ErrorModel> {
        val targetUserRequest = getUserUseCase(serviceOwnerUid)
        val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
        return if (targetUserRequest is Success && currentUserRequest is Success) {
            val senderInfo = Pair(currentUserRequest.result.id, currentUserRequest.result.name)
            val serviceInfo = Pair(serviceId, targetUserRequest.result.id.toString())
            NewValue(listOf(senderInfo, serviceInfo))
        } else {
            Error(ErrorModel(""))
        }
    }
}