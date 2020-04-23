package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.push.ChatNotificationModel
import com.napptilians.domain.usecases.SendNotificationUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ChatViewModel @Inject constructor(
    private val sendNotificationUseCase: SendNotificationUseCase
) : BaseViewModel<ChatNotificationModel>() {

    private val _sendNotificationDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val sendNotificationDataStream: LiveData<UiStatus<Unit, ErrorModel>>
        get() = _sendNotificationDataStream

    fun execute(notification: ChatNotificationModel, topic: String) {
        viewModelScope.launch {
            _sendNotificationDataStream.value = emitLoadingState()
            val request = sendNotificationUseCase.execute(notification, topic)
            _sendNotificationDataStream.value = processModel(request)
        }
    }
}
