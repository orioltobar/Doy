package com.napptilians.doy.view.chat.detail

import android.view.ViewGroup
import com.napptilians.domain.models.chat.MessageModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ChatAdapter @Inject constructor() : BaseAdapter<MessageModel, ChatMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(parent)
    }
}
