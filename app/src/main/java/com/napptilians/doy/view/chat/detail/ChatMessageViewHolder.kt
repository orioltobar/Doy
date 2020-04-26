package com.napptilians.doy.view.chat.detail

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageBox
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageRootView
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderSenderName
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderText
import kotlinx.android.synthetic.main.chat_message_view_holder.view.chatMessageViewHolderTimestamp
import java.text.SimpleDateFormat
import java.util.*

class ChatMessageViewHolder(private val parent: ViewGroup, private val userId: String) :
    BaseViewHolder<ChatModel>(parent, R.layout.chat_message_view_holder) {

    override fun update(model: ChatModel) {
        if (model.senderId == userId) {
            itemView.chatMessageBox.background = ContextCompat.getDrawable(parent.context, R.drawable.bg_solid_rectangle)
            itemView.chatMessageRootView.gravity = Gravity.END
        } else {
            itemView.chatMessageBox.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.light_gray))
            itemView.chatMessageRootView.gravity = Gravity.START
        }

        itemView.chatMessageViewHolderSenderName.text = model.senderName
        itemView.chatMessageViewHolderText.text = model.message

        val time = Date(model.timeStamp)
        val formattedTime = SimpleDateFormat("HH:mm")
        itemView.chatMessageViewHolderTimestamp.text = formattedTime.format(time)
    }
}
