package com.napptilians.doy.view.chat.detail

import android.view.ViewGroup
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ChatAdapter @Inject constructor() :
    BaseAdapter<ChatModel, ChatMessageViewHolder>() {

    private var userId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(parent, userId)
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }
}
