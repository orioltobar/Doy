package com.napptilians.doy.view.chat

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemEventName
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemMessageCounterContainer
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoCardView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoShape
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemPhotoView
import kotlinx.android.synthetic.main.chat_list_view_holder.view.profilePhotoSmiley

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
    }
}