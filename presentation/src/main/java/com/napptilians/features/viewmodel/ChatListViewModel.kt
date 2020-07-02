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
import org.threeten.bp.ZonedDateTime
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

    fun getChatInformation(
        serviceId: Long,
        serviceName: String,
        serviceStartDate: ZonedDateTime,
        serviceDuration: Int
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
                        serviceStartDate,
                        serviceDuration
                    )
                    _userDataStream.setValue(NewValue(requestModel))
                },
                onFailure = {
                    _userDataStream.setValue(Error(ErrorModel("")))
                }
            )
        }
    }

    private suspend fun processServicesResponse(request: Response<List<ServiceModel>, ErrorModel>)
            : Response<Map<String, List<ChatListItemModel>>, ErrorModel> =
        supervisorScope {
            if (request is Success) {
                val filteredResult: MutableMap<String, List<ChatListItemModel>> = mutableMapOf()
                val services = request.result

                val upcomingChats = mutableListOf<ChatListItemModel>()
                val activeChats = mutableListOf<ChatListItemModel>()
                val pastChats = mutableListOf<ChatListItemModel>()
                services.map { service ->
                    val chat = getChatListItemModel(service, null)
                    when (chat.status) {
                        ChatListItemModel.Status.Upcoming -> {
                            upcomingChats.add(chat)
                        }
                        ChatListItemModel.Status.Active -> {
                            activeChats.add(chat)
                        }
                        ChatListItemModel.Status.Past -> {
                            pastChats.add(chat)
                        }
                        ChatListItemModel.Status.Unknown -> {
                        }
                    }
                    //chatUiList.add(element)
                    viewModelScope.launch {
                        initChatObserver(service)
                    }
                }

                Success(
                    mapOf(
                        ChatListItemModel.UPCOMING to upcomingChats,
                        ChatListItemModel.ACTIVE to activeChats,
                        ChatListItemModel.PAST to pastChats
                    )
                )
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
        response: Response<Pair<ChatModel, Int>, ErrorModel>?
    ): ChatListItemModel {

        val chatModel = (response as? Success)?.result?.first
        val unreadMessages = (response as? Success)?.result?.second
        return ChatListItemModel(
            service.serviceId ?: -1L,
            service.name ?: "",
            service.image ?: "",
            chatModel?.senderName ?: "",
            chatModel?.message ?: "",
            chatModel?.timeStamp?.let { DateFormatter.format(it) }
                ?: "",
            ChatListItemModel.getCurrentStatus(service.date, service.durationMin),
            chatModel?.read ?: false,
            unreadMessages ?: 0,
            service.date ?: ZonedDateTime.now(),
            service.durationMin ?: 0
        )
    }
}