package com.napptilians.doy.view.chat

import android.view.ViewGroup
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ChatListAdapter @Inject constructor() : BaseAdapter<ChatListItemModel, ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(parent)
    }

    fun updateItem(newItem: ChatListItemModel) {
        val list = getItems().map { item ->
            if (item.id == newItem.id && newItem.lastSenderName.isNotEmpty())
                newItem
            else
                item
        }
        updateItems(list)
    }
}
