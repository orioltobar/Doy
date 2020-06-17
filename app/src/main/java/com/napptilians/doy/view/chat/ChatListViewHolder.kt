package com.napptilians.doy.view.chat

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemEventName
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemLastMessageDate
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemLastMessageText
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemLastMessageTextBold
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemMessageCounterContainer
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemMessageCounterText
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoCardView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoShape
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.profilePhotoSmiley

class ChatListViewHolder(viewGroup: ViewGroup) :
    BaseViewHolder<ChatListItemModel>(viewGroup, R.layout.chat_list_view_holder) {

    override fun update(model: ChatListItemModel) {
        // Event Name
        itemView.chatListItemEventName.text = model.name

        // Image
        val urlRegex = android.util.Patterns.WEB_URL
        val isUrl = urlRegex.matcher(model.imageUrl).find()
        model.imageUrl.takeIf { isUrl }?.let {
            itemView.chatListItemPhotoCardView.visible()
            Glide.with(itemView.chatListItemPhotoShape)
                .load(it)
                .placeholder(R.drawable.ic_smile)
                .into(itemView.chatListItemPhotoView)
            itemView.profilePhotoSmiley.gone()
        } ?: run { itemView.profilePhotoSmiley.visible() }

        // Sender and message container
        if (model.lastSenderName.isNotEmpty() && model.lastMessage.isNotEmpty()) {
            val senderAndMessage = "${model.lastSenderName}: ${model.lastMessage}"
            if (model.read) {
                itemView.chatListItemLastMessageText.text = senderAndMessage
                itemView.chatListItemLastMessageTextBold.gone()
                itemView.chatListItemLastMessageText.visible()
            } else {
                itemView.chatListItemLastMessageTextBold.text = senderAndMessage
                itemView.chatListItemLastMessageText.gone()
                itemView.chatListItemLastMessageTextBold.visible()
            }
        } else {
            itemView.chatListItemLastMessageTextBold.gone()
            itemView.chatListItemLastMessageText.gone()
        }

        // Message timestamp
        model.lastMessageTime.takeIf { it.isNotEmpty() }?.let { date ->
            itemView.chatListItemLastMessageDate.text = date
            itemView.chatListItemLastMessageDate.visible()
        } ?: run {
            itemView.chatListItemLastMessageDate.gone()
        }

        // Unread messages
        if (model.unreadMessages > 0) updateMessageCounter(model.unreadMessages)
        else itemView.chatListItemMessageCounterContainer.gone()
    }

    private fun updateMessageCounter(messages: Int) {
        when {
            messages in 1..9 -> {
                itemView.chatListItemMessageCounterText.text = messages.toString()
                itemView.chatListItemMessageCounterContainer.visible()
            }
            messages >= 10 -> {
                itemView.chatListItemMessageCounterText.text = "+9"
                itemView.chatListItemMessageCounterContainer.visible()
            }
            else -> {
                itemView.chatListItemMessageCounterContainer.gone()
            }
        }
    }
}