package com.napptilians.doy.view.chat

import android.view.ViewGroup
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemEventName
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemLastMessageText
import kotlinx.android.synthetic.main.chat_list_view_holder.view.chatListItemMessageCounterContainer

class ChatListViewHolder(viewGroup: ViewGroup) : BaseViewHolder<ServiceModel>(viewGroup, R.layout.chat_list_view_holder) {

    override fun update(model: ServiceModel) {
        itemView.chatListItemEventName.text = model.name
        itemView.chatListItemLastMessageText.text = "${model.description} id: ${model.serviceId}"
        itemView.chatListItemMessageCounterContainer.gone()
    }
}