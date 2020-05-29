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
import com.google.common.hash.Hashing
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
import kotlinx.android.synthetic.main.chat_fragment.*
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.net.URL
import javax.inject.Inject

@Suppress("UnstableApiUsage")
class ChatFragment : BaseFragment() {

    private val viewModel: ChatViewModel by viewModels { vmFactory }

    private val args: ChatFragmentArgs by navArgs()
    private var chatRequestModel: ChatRequestModel? = null
    private var currentUserId: Long = 0
    private var serviceId: Long = 0
    private var senderName: String = ""
    private var serviceName: String = ""
    private lateinit var serviceStartDate: ZonedDateTime

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
        currentUserId = chatRequestModel?.currentUserId ?: args.request.currentUserId
        serviceId = chatRequestModel?.serviceId ?: args.request.serviceId
        senderName = chatRequestModel?.senderName ?: args.request.senderName
        serviceName = chatRequestModel?.serviceName ?: args.request.serviceName
        serviceStartDate = chatRequestModel?.serviceStartDate ?: args.request.serviceStartDate

        chatFragmentHeaderTitle.text = serviceName

        initVideoChatSettings()

        chatFragmentVideoChatButton.setOnClickListener {
            navigateToVideoChat()
        }

        val chatId = serviceId.toString()
        adapter.setUserId(currentUserId.toString())
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

        if (checkCurrentDate()) {
            chatFragmentVideoChatContainer.visible()
        } else {
            chatFragmentVideoChatContainer.gone()
        }
        viewModel.getMessagesFromChat(chatId)
        viewModel.messageFlow.observe(viewLifecycleOwner, Observer { result ->
            handleUiStates(result, ::processResponse)
        })
    }

    private fun sendData(text: String) {
        val senderId = currentUserId.toString()
        val senderName = senderName
        val chatId = serviceId.toString()

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

    private fun initVideoChatSettings() {
        val serverURL = URL(VIDEO_CHAT_URL)
        val defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
    }

    private fun buildVideoChatRoom(service: String): String {
        val hashedString = Hashing.murmur3_32()
            .newHasher()
            .putString(service, Charsets.UTF_8)
            .hash().asInt().toString()

        val appName = getString(R.string.app_name)
        return "$appName - $service - $hashedString"
    }

    private fun navigateToVideoChat() {
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom(buildVideoChatRoom(serviceName))
            .setUserInfo(JitsiMeetUserInfo(Bundle().apply {
                putString(DISPLAY_NAME_KEY, senderName)
            }))
            .build()

        JitsiMeetActivity.launch(context, options)
    }

    private fun checkCurrentDate(): Boolean {
        val currentDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault())
        val threshold = serviceStartDate.withZoneSameInstant(ZoneId.systemDefault())
            .minusMinutes(Notifications.MINUTES)
        return currentDate >= threshold
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

    companion object {
        private const val VIDEO_CHAT_URL = "https://meet.jit.si"
        private const val DISPLAY_NAME_KEY = "displayName"
    }
}
