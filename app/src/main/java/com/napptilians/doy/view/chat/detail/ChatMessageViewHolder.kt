package com.napptilians.doy.view.chat.detail

import android.view.ViewGroup
import com.napptilians.domain.models.chat.MessageModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.chat_message_view_holder.view.*

class ChatMessageViewHolder(parent: ViewGroup) : BaseViewHolder<MessageModel>(parent, R.layout.chat_message_view_holder) {

    override fun update(model: MessageModel) {
        itemView.chatMessageViewHolderText.text = model.text
        itemView.chatMessageViewHolderTimestamp.text = model.timeStamp.toString()
    }
}
