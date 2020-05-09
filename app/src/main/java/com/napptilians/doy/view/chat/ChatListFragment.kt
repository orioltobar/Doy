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
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.chat_list_fragment.chatListNoChatMessage
import kotlinx.android.synthetic.main.chat_list_fragment.chatListProgressView
import kotlinx.android.synthetic.main.chat_list_fragment.chatListRecyclerView
import javax.inject.Inject

class ChatListFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ChatListAdapter

    private val viewModel: ChatListViewModel by viewModels { vmFactory }

    private var serviceSelectedName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatListRecyclerView.adapter = adapter

        adapter.setOnClickListener { serviceModel ->
            serviceSelectedName = serviceModel.name ?: ""

            viewModel.getTargetUser(
                serviceModel.serviceId ?: -1L,
                serviceModel.ownerId ?: ""
            )
        }

        // SingleLiveEvent Observer
        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<Pair<Long, String>>, ErrorModel>> { status ->
                handleUiStates(status) { processTargetUser(it) }
            }
        )

        // LiveData Observer
        viewModel.chatListDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<ServiceModel>, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )
    }

    override fun onError(error: ErrorModel) {
        chatListProgressView.gone()
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

    private fun processTargetUser(chatUsersInfo: List<Pair<Long, String>>) {
        chatUsersInfo.takeIf { it.size >= 2 }?.let {
            val direction = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
                it[0].first,  // current user Id
                it[1].first,  // service Id
                it[0].second, // sender name
                serviceSelectedName  // event name
            )
            findNavController().navigate(direction)
        }
    }
}
