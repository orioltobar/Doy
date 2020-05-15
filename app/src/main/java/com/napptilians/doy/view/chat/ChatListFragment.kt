package com.napptilians.doy.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetChatsUseCase
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.doy.view.events.PagerAdapter
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.chat_list_fragment.chatListTitle
import kotlinx.android.synthetic.main.chat_list_fragment.chatsTabLayout
import kotlinx.android.synthetic.main.chat_list_fragment.chatsViewPager
import javax.inject.Inject

class ChatListFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ChatListAdapter

    private lateinit var chatsAdapter: PagerAdapter

    private val viewModel: ChatListViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.chat_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        // SingleLiveEvent Observer
        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<ChatRequestModel, ErrorModel>> { status ->
                handleUiStates(status) { processTargetUser(it) }
            }
        )

        // LiveData Observer
        viewModel.chatListDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )
    }

    private fun initViews() {
        chatListTitle.visible()
        chatListTitle.text = context?.resources?.getText(R.string.chat)

        chatsAdapter = PagerAdapter(childFragmentManager)
        chatsAdapter.apply {
            addFragment(
                ChatTabFragment { navigateToChat(it) },
                getString(R.string.tab_upcoming)
            )
            addFragment(
                ChatTabFragment { navigateToChat(it) },
                getString(R.string.tab_past)
            )
        }
        chatsViewPager.adapter = chatsAdapter
        chatsViewPager.offscreenPageLimit = 2
        chatsTabLayout.setupWithViewPager(chatsViewPager)
    }

    override fun onError(error: ErrorModel) {
        chatsViewPager.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
    }

    private fun processNewValue(model: Map<String, List<ServiceModel>>) {
        chatsViewPager.visible()
        (chatsAdapter.getItem(0) as ChatTabFragment).setItems(
            model[GetChatsUseCase.UPCOMING] ?: listOf()
        )
        (chatsAdapter.getItem(1) as ChatTabFragment).apply {
            setItems(model[GetChatsUseCase.PAST] ?: listOf())
            setAlphaToPastChats(0.4f)
        }
    }

    private fun processTargetUser(requestModel: ChatRequestModel) {
        val direction = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
            requestModel
        )
        findNavController().navigate(direction)
    }

    private fun navigateToChat(serviceModel: ServiceModel) {
        viewModel.getChatInformation(
            serviceModel.serviceId ?: -1L,
            serviceModel.name ?: ""
        )
    }
}
