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
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemMessageCounterContainer
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoCardView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoShape
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.profilePhotoSmiley
import org.threeten.bp.Instant
import java.text.SimpleDateFormat
import java.util.*

class ChatListViewHolder(viewGroup: ViewGroup) :
    BaseViewHolder<ChatListItemModel>(viewGroup, R.layout.chat_list_view_holder) {

    override fun update(model: ChatListItemModel) {
        // Event Name
        itemView.chatListItemEventName.text = model.name

        // TODO: Message counter
        itemView.chatListItemMessageCounterContainer.gone()

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

        if (model.lastSenderName.isNotEmpty() && model.lastMessage.isNotEmpty()) {
            val senderAndMessage = "${model.lastSenderName}: ${model.lastMessage}"
            itemView.chatListItemLastMessageText.text = senderAndMessage
            itemView.chatListItemLastMessageText.visible()
        } else {
            itemView.chatListItemLastMessageText.gone()
        }

        model.lastMessageTime.toLongOrNull()?.let { safeLong ->
            itemView.chatListItemLastMessageDate.text = dateFormatter(safeLong)
            itemView.chatListItemLastMessageDate.visible()
        } ?: run {
            itemView.chatListItemLastMessageDate.gone()
        }
    }

    // TODO: Move from here. Not working.
    private fun dateFormatter(timestamp: Long): String {
        val lastMessageSeconds = Instant.ofEpochMilli(timestamp).epochSecond
        val currentSeconds = Instant.now().epochSecond
        val difference = currentSeconds - lastMessageSeconds

        return if (difference < 60) {
            // SECONDS
            "${difference}s"
        } else if (difference > 60 && difference < (60 * 60)) {
            // Minutes
            "${difference}m"
        } else if (difference > (60 * 60) && difference < (60 * 60 * 24)) {
            // Hours
            "${difference}h"
        } else {
            // Date
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            formatter.format(Date(timestamp))
        }
    }
}