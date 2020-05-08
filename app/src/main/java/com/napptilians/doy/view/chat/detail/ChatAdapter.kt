package com.napptilians.doy.view.chat.detail

import android.view.ViewGroup
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ChatAdapter @Inject constructor() :
    BaseAdapter<ChatModel, ChatMessageViewHolder>() {

    private var userId: String = ""

    private val chatMessages: MutableList<ChatModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(parent, userId)
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun updateItem(message: ChatModel) {
        chatMessages.takeIf { !it.contains(message) }?.let {
            chatMessages.add(message)
        }
        chatMessages.filter { it.message.isNotEmpty() }
        updateItems(chatMessages)
    }
}
