package com.napptilians.doy.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.MessageModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.chat_fragment.*

class ChatFragment : BaseFragment() {

    private val auth: FirebaseAuth? get() = FirebaseAuth.getInstance()
    private val user: FirebaseUser? get() = FirebaseAuth.getInstance().currentUser
    private val databaseReference: DatabaseReference? by lazy { FirebaseDatabase.getInstance().reference }

    @Inject
    lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user?.let {
            println("FIREBASE: ${it.displayName}")
            println("FIREBASE: ${it.photoUrl}")
        } ?: run {
            println("FIREBASE: NOT LOGGED!")
        }

        createFireBaseListener()
        fireBaseChatMessages.adapter = adapter

        chatFragmentSendButton.setOnClickListener {
            if (chatFragmentEditText.text.isNotEmpty()) {
                sendData(chatFragmentEditText.text.toString())
            }
        }
    }

    private fun sendData(text: String) {
        val message = MessageModel(text)
        databaseReference?.child("messages")?.push()?.setValue(message)
        chatFragmentEditText.setText("")
    }

    private fun refreshRecycler(messages: List<MessageModel>) {
        adapter.updateItems(messages)
        adapter.notifyDataSetChanged()
        fireBaseChatMessages.scrollToPosition(messages.size - 1)
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private fun createFireBaseListener() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val toReturn: MutableList<MessageModel> = mutableListOf()
                for (data in p0.children) {
                    val messageData = data.getValue<MessageModel>(MessageModel::class.java)
                    val message = messageData?.let { it } ?: continue

                    toReturn.add(message)
                }
                toReturn.sortBy { message -> message.timeStamp }

                refreshRecycler(toReturn.toList())
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }

        databaseReference?.child("messages")?.addValueEventListener(postListener)
    }
}
