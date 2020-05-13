package com.napptilians.doy.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.chat_tab_fragment.chatsList
import kotlinx.android.synthetic.main.chat_tab_fragment.chatsLoadingProgress
import kotlinx.android.synthetic.main.chat_tab_fragment.chatsLoadingText
import javax.inject.Inject

class ChatTabFragment(
    private val onChatClicked: (ChatListItemModel) -> Unit = {}
) : BaseFragment() {

    @Inject
    lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_tab_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun setItems(chats: List<ChatListItemModel>) {
        if (chats.isNotEmpty()) {
            chatListAdapter.updateItems(chats)
            chatsList.visible()
            chatsLoadingProgress.gone()
            chatsLoadingText.gone()
        } else {
            chatsList.gone()
            chatsLoadingProgress.gone()
            chatsLoadingText.text = context?.resources?.getText(R.string.no_chats)
            chatsLoadingText.visible()
        }
    }

    fun setAlphaToPastChats(alpha: Float) {
        chatsList.alpha = alpha
    }

    override fun onError(error: ErrorModel) {}

    override fun onLoading() {}

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        chatsList.layoutManager = layoutManager
        chatListAdapter = ChatListAdapter()
        chatListAdapter.setOnClickListener {
            onChatClicked(it)
        }
        chatsList.adapter = chatListAdapter
    }
}