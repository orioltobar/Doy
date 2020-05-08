package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.Failure
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.usecases.GetChatMessageUseCase
import com.napptilians.domain.usecases.SendChatMessageUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val getChatMessageUseCase: GetChatMessageUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase
) : BaseViewModel<ChatModel>() {

    private val _messageFlow = MutableLiveData<UiStatus<ChatModel, ErrorModel>>()
    val messageFlow: LiveData<UiStatus<ChatModel, ErrorModel>> get() = _messageFlow

    fun getMessagesFromChat(chatId: String) {
        _messageFlow.value = emitLoadingState()
        viewModelScope.launch {
            getChatMessageUseCase(chatId).collect { response ->
                _messageFlow.value = processModel(response)
            }
        }
    }

    fun sendMessageToChat(
        chatId: String,
        senderId: String,
        sendeName: String,
        messageContent: String
    ) {
        viewModelScope.launch {
            val message = ChatModel(chatId, messageContent, sendeName, senderId)
            val response = sendChatMessageUseCase(chatId, message)
            if (response is Failure) {
                // Process the sent message only if the request failed.
                _messageFlow.value = processModel(response)
            }
        }
    }
}