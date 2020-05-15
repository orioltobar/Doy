package com.napptilians.doy.view.chat.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.FirebaseErrors
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.Notifications
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentEditText
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentHeaderTitle
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentProgressView
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentSendButton
import kotlinx.android.synthetic.main.chat_fragment.fireBaseChatMessages
import javax.inject.Inject

class ChatFragment : BaseFragment() {

    private val viewModel: ChatViewModel by viewModels { vmFactory }

    private val args: ChatFragmentArgs by navArgs()
    private var chatRequestModel: ChatRequestModel? = null

    @Inject
    lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatRequestModel = (arguments?.get(Notifications.CHAT_REQUEST_KEY) as? ChatRequestModel)
        chatFragmentHeaderTitle.text = chatRequestModel?.serviceName ?: args.request.serviceName

        val chatId = chatRequestModel?.serviceId?.toString() ?: args.request.serviceId.toString()
        adapter.setUserId(
            chatRequestModel?.currentUserId?.toString() ?: args.request.currentUserId.toString()
        )
        fireBaseChatMessages.adapter = adapter
        // Show bottom messages first: https://stackoverflow.com/a/27069845
        (fireBaseChatMessages?.layoutManager as? LinearLayoutManager)?.stackFromEnd = true

        chatFragmentSendButton.setOnClickListener {
            if (chatFragmentEditText.text?.isNotEmpty() == true) {
                sendData(chatFragmentEditText.text.toString())
            }
        }

        chatFragmentSendButton.isEnabled = false
        chatFragmentEditText.addTextChangedListener {
            chatFragmentSendButton.isEnabled = it?.toString()?.isNotBlank() == true
        }

        viewModel.getMessagesFromChat(chatId)
        viewModel.messageFlow.observe(viewLifecycleOwner, Observer { result ->
            handleUiStates(result, ::processResponse)
        })
    }

    private fun sendData(text: String) {
        val senderId =
            chatRequestModel?.currentUserId?.toString() ?: args.request.currentUserId.toString()
        val senderName = chatRequestModel?.senderName ?: args.request.senderName
        val chatId = chatRequestModel?.serviceId?.toString() ?: args.request.serviceId.toString()

        viewModel.sendMessageToChat(chatId, senderId, senderName, text)
        chatFragmentEditText.setText("")
    }

    private fun processResponse(response: ChatModel) {
        chatFragmentProgressView.gone()
        refreshRecycler(response)
    }

    private fun refreshRecycler(messages: ChatModel) {
        adapter.updateItem(messages)
        // Apply smooth scroll in order to make it work: https://stackoverflow.com/a/37719465
        val nonEmptyMessages = adapter.getItems().filter { it.message.isNotEmpty() }
        if (nonEmptyMessages.isNotEmpty()) {
            fireBaseChatMessages?.smoothScrollToPosition(nonEmptyMessages.size - 1)
        }
    }

    override fun onError(error: ErrorModel) {
        chatFragmentProgressView.gone()
        when (error.errorCause) {
            FirebaseErrors.EmptyMessage -> {
            }
            else -> {
                activity?.let {
                    val dialog = DoyErrorDialog(it)
                    dialog.show()
                    dialog.setOnDismissListener { findNavController().popBackStack() }
                }
            }
        }
    }

    override fun onLoading() {
        chatFragmentProgressView.visible()
    }
}
