package com.napptilians.doy.view.chat.detail

import android.view.ViewGroup
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderSenderName
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderText
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderTimestamp
import java.text.SimpleDateFormat
import java.util.*

class ChatMessageViewHolder(parent: ViewGroup) :
    BaseViewHolder<ChatModel>(parent, R.layout.chat_message_view_holder) {

    override fun update(model: ChatModel) {
        itemView.chatMessageViewHolderSenderName.text = model.senderName

        itemView.chatMessageViewHolderText.text = model.message

        val time = Date(model.timeStamp)
        val formattedTime = SimpleDateFormat("HH:mm")
        itemView.chatMessageViewHolderTimestamp.text = formattedTime.format(time)
    }
}
