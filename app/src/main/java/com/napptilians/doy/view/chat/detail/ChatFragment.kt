package com.napptilians.doy.view.chat.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.view.customviews.DoyErrorDialog
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentEditText
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentHeaderTitle
import kotlinx.android.synthetic.main.chat_fragment.chatFragmentSendButton
import kotlinx.android.synthetic.main.chat_fragment.fireBaseChatMessages
import javax.inject.Inject

// TODO: Re do this view after MVP.
class ChatFragment : BaseFragment() {

    private var databaseReference: DatabaseReference? = null

    private val args: ChatFragmentArgs by navArgs()

    private lateinit var firebaseListener: ValueEventListener

    @Inject
    lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatFragmentHeaderTitle.text = args.serviceTitle

        val chatId = args.serviceId.toString()
        databaseReference = FirebaseDatabase.getInstance().reference
        firebaseListener = createFireBaseListener()
        databaseReference?.child(MESSAGE_TABLE_NAME)?.child(chatId)
            ?.addValueEventListener(firebaseListener)

        adapter.setUserId(args.userId.toString())
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
    }

    override fun onDestroy() {
        databaseReference?.removeEventListener(firebaseListener)
        super.onDestroy()
    }

    private fun sendData(text: String) {
        val senderName = args.senderName
        val chatId = args.serviceId.toString()

        val message = ChatModel(chatId, text, senderName, args.userId.toString())
        databaseReference?.child(MESSAGE_TABLE_NAME)?.child(chatId)?.push()?.setValue(message)
        chatFragmentEditText.setText("")
    }

    private fun refreshRecycler(messages: List<ChatModel>) {
        adapter.updateItems(messages.filter { it.message.isNotEmpty() })
        adapter.notifyDataSetChanged()
        // Apply smooth scroll in order to make it work: https://stackoverflow.com/a/37719465
        fireBaseChatMessages?.smoothScrollToPosition(messages.size - 1)
    }

    override fun onError(error: ErrorModel) {
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
    }

    private fun createFireBaseListener() = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val toReturn: MutableList<ChatModel> = mutableListOf()
            for (data in dataSnapshot.children) {
                val messageData = data.getValue<ChatModel>(ChatModel::class.java)
                val message = messageData?.let { it } ?: continue

                toReturn.add(message)
            }
            toReturn.sortBy { message -> message.timeStamp }

            refreshRecycler(toReturn.toList())
        }

        override fun onCancelled(p0: DatabaseError) {
        }
    }

    companion object {
        private const val MESSAGE_TABLE_NAME = "messages"
    }
}
