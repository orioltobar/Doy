package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetChatsUseCase
import com.napptilians.domain.usecases.GetLastChatMessageUseCase
import com.napptilians.domain.usecases.GetUserUseCase
import com.napptilians.features.Error
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.base.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.ConcurrentLinkedDeque
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatsUseCase: GetChatsUseCase,
    private val lastChatMessageUseCase: GetLastChatMessageUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<ChatListItemModel>() {

    private val _chatListDataStream =
        MutableLiveData<UiStatus<Map<String, List<ChatListItemModel>>, ErrorModel>>()
    val chatListDataStream: LiveData<UiStatus<Map<String, List<ChatListItemModel>>, ErrorModel>>
        get() = _chatListDataStream

    private val _userDataStream = SingleLiveEvent<UiStatus<ChatRequestModel, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<ChatRequestModel, ErrorModel>> get() = _userDataStream

    init {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                _chatListDataStream.value = emitLoadingState()
                val request = chatsUseCase.execute(emptyList(), null, it.uid, false)
                val result = processServicesResponse(request)
                _chatListDataStream.value = processModel(result)
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

    private suspend fun processServicesResponse(request: Response<Map<String, List<ServiceModel>>, ErrorModel>)
            : Response<Map<String, List<ChatListItemModel>>, ErrorModel> =
        supervisorScope {
            if (request is Success) {
                val filteredResult: MutableMap<String, List<ChatListItemModel>> = mutableMapOf()
                val mapOfEvents = request.result
                for ((key, services) in mapOfEvents) {
                    val chatUiList = ConcurrentLinkedDeque<ChatListItemModel>()
                    services.map { service ->
                        async(Dispatchers.IO) {
                            val lastMessageRequest =
                                lastChatMessageUseCase(service.serviceId.toString())
                            chatUiList.add(
                                ChatListItemModel(
                                    service.serviceId ?: -1L,
                                    service.name ?: "",
                                    service.image ?: "",
                                    (lastMessageRequest as? Success)?.result?.senderName ?: "",
                                    (lastMessageRequest as? Success)?.result?.message ?: "",
                                    "" // TODO: Add the hour of the message formatted.
                                )
                            )
                        }
                    }.awaitAll()
                    filteredResult[key] = chatUiList.toList()
                }
                Success(filteredResult)
            } else {
                Failure((request as Failure).error)
            }
        }
}