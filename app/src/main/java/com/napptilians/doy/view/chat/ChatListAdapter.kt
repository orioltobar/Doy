package com.napptilians.doy.view.chat

import android.view.ViewGroup
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ChatListAdapter @Inject constructor() : BaseAdapter<ChatListItemModel, ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(parent)
    }
}