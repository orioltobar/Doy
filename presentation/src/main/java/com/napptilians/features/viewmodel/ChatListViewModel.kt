package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.DateFormatter
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetChatsUseCase
import com.napptilians.domain.usecases.GetLastChatMessageUseCase
import com.napptilians.domain.usecases.GetUserUseCase
import com.napptilians.features.Error
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.base.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val chatsUseCase: GetChatsUseCase,
    private val lastChatMessageUseCase: GetLastChatMessageUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<ChatListItemModel>() {

    private val _chatListDataStream =
        SingleLiveEvent<UiStatus<Map<String, List<ChatListItemModel>>, ErrorModel>>()
    val chatListDataStream: LiveData<UiStatus<Map<String, List<ChatListItemModel>>, ErrorModel>>
        get() = _chatListDataStream

    private val _chatUpdateDataStream =
        SingleLiveEvent<UiStatus<ChatListItemModel, ErrorModel>>()
    val chatUpdateDataStream: LiveData<UiStatus<ChatListItemModel, ErrorModel>>
        get() = _chatUpdateDataStream

    private val _userDataStream = SingleLiveEvent<UiStatus<ChatRequestModel, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<ChatRequestModel, ErrorModel>> get() = _userDataStream

    fun getChats() {
        viewModelScope.launch {
            firebaseAuth.currentUser?.let {
                _chatListDataStream.setValue(emitLoadingState())
                val request = chatsUseCase.execute(emptyList(), null, it.uid, false)
                val result = processServicesResponse(request)
                _chatListDataStream.setValue(processModel(result))
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
                    val chatUiList = mutableListOf<ChatListItemModel>()
                    services.mapIndexed { index, service ->
                        chatUiList.add(getChatListItemModel(service, null))
                        viewModelScope.launch {
                            initChatObserver(service)
                        }
                    }
                    filteredResult[key] = chatUiList.toList()
                }
                Success(filteredResult)
            } else {
                Failure((request as Failure).error)
            }
        }

    private suspend fun initChatObserver(service: ServiceModel) {
        lastChatMessageUseCase(service.serviceId.toString())
            .collect { newValue ->
                if (newValue is Success) {
                    val result = getChatListItemModel(service, newValue)
                    _chatUpdateDataStream.setValue(NewValue(result))
                }
            }
    }

    private fun getChatListItemModel(
        service: ServiceModel,
        response: Response<ChatModel, ErrorModel>?
    ) =
        ChatListItemModel(
            service.serviceId ?: -1L,
            service.name ?: "",
            service.image ?: "",
            (response as? Success)?.result?.senderName ?: "",
            (response as? Success)?.result?.message ?: "",
            (response as? Success)?.result?.timeStamp?.let { DateFormatter.format(it) } ?: "",
            ChatListItemModel.getCurrentStatus(service.date)
        )
}