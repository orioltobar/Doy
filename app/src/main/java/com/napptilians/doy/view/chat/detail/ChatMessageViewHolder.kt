package com.napptilians.doy.view.chat.detail

import android.view.Gravity
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.marginDp
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
            itemView.chatMessageBox.marginDp(left = MESSAGE_BOX_HORIZONTAL_MARGIN_DP, right = 0f)
        } else {
            itemView.chatMessageBox.background = ContextCompat.getDrawable(parent.context, R.drawable.bg_gray_rectangle)
            itemView.chatMessageRootView.gravity = Gravity.START
            itemView.chatMessageBox.marginDp(left = 0f, right = MESSAGE_BOX_HORIZONTAL_MARGIN_DP)
        }

        itemView.chatMessageViewHolderSenderName.text = model.senderName
        itemView.chatMessageViewHolderText.text = model.message

        val time = Date(model.timeStamp)
        val formattedTime = SimpleDateFormat("HH:mm")
        itemView.chatMessageViewHolderTimestamp.text = formattedTime.format(time)
    }

    companion object {
        private const val MESSAGE_BOX_HORIZONTAL_MARGIN_DP = 48f
    }
}
