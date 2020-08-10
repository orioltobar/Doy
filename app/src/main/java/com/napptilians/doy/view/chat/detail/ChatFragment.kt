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
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentEditText
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentHeaderTitle
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentLoadingProgress
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentLoadingText
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentSendButton
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentVideoChatButton
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentVideoChatContainer
import kotlinx.android.synthetic.main.chat_fragment.fireBaseChatMessages
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
    private var serviceDuration: Int = 0
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
        serviceDuration = chatRequestModel?.serviceDuration ?: args.request.serviceDuration

        chatFragmentHeaderTitle.text = serviceName

        initVideoChatSettings()

        if (checkCurrentDate()) {
            chatFragmentVideoChatContainer.visible()
        } else {
            chatFragmentVideoChatContainer.gone()
        }

        chatFragmentVideoChatButton.setOnClickListener {
            navigateToVideoChat()
        }

        val chatId = serviceId.toString()
        adapter.setUserId(currentUserId.toString())
        fireBaseChatMessages.adapter = adapter

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
        val senderId = currentUserId.toString()
        val senderName = senderName
        val chatId = serviceId.toString()

        viewModel.sendMessageToChat(chatId, senderId, senderName, text)
        chatFragmentEditText.setText("")
    }

    private fun processResponse(response: ChatModel) {
        chatFragmentLoadingProgress.gone()
        chatFragmentLoadingText.gone()
        refreshRecycler(response)
    }

    private fun refreshRecycler(messages: ChatModel) {
        adapter.updateItem(messages)
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
        val zoneId = ZoneId.systemDefault()
        val currentDate = ZonedDateTime.now().withZoneSameInstant(zoneId)
        val startDate = serviceStartDate.withZoneSameInstant(zoneId)
        val endDate = serviceStartDate.withZoneSameInstant(zoneId)
            .plusMinutes(serviceDuration.toLong())
        return currentDate in startDate..endDate
    }

    override fun onError(error: ErrorModel) {
        chatFragmentLoadingProgress.gone()
        chatFragmentLoadingText.gone()
        when (error.errorCause) {
            FirebaseErrors.EmptyChat -> {
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
        chatFragmentLoadingProgress.visible()
        chatFragmentLoadingText.visible()
    }

    companion object {
        private const val VIDEO_CHAT_URL = "https://meet.jit.si"
        private const val DISPLAY_NAME_KEY = "displayName"
    }
}
