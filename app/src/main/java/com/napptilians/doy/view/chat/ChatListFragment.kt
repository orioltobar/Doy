package com.napptilians.doy.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.chat_list_fragment.chatListNoChatMessage
import kotlinx.android.synthetic.main.chat_list_fragment.chatListProgressView
import kotlinx.android.synthetic.main.chat_list_fragment.chatListRecyclerView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Re do this view after MVP.
class ChatListFragment : BaseFragment() {

    // TODO: WORKAROUND FOR NAVIGATION TO CHAT VIEW. THIS IS NOT MEANT TO BE HERE!!!
    private val job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, _ -> }

    private val coroutineScope: CoroutineScope =
        CoroutineScope(job + Dispatchers.Main + errorHandler)

    @Inject
    lateinit var adapter: ChatListAdapter

    private val viewModel: ChatListViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatListRecyclerView.adapter = adapter

        adapter.setOnClickListener { serviceModel ->
            coroutineScope.launch {
                val values = viewModel.retrieveChatParameters(
                    serviceModel.serviceId ?: -1L,
                    serviceModel.ownerId ?: ""
                )
                if (values is NewValue) {
                    processTargetUser(values.result, serviceModel.name ?: "")
                }
            }
        }

        // LiveData Observer
        viewModel.chatListDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<ServiceModel>, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onError(error: ErrorModel) {
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        chatListProgressView.visible()
    }

    private fun processNewValue(models: List<ServiceModel>) {
        chatListProgressView.gone()
        models.takeIf { it.isNotEmpty() }?.let {
            adapter.updateItems(it)
            adapter.notifyDataSetChanged()
        } ?: run { chatListNoChatMessage.visible() }
    }

    private fun processTargetUser(chatUsersInfo: List<Pair<Long, String>>, serviceTitle: String) {
        chatUsersInfo.takeIf { it.size >= 2 }?.let {
            val direction = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                it[0].first,  // current user Id
                it[1].first,  // service Id
                it[0].second, // sender name
                serviceTitle  // event name
            )
            findNavController().navigate(direction)
        }
    }
}
