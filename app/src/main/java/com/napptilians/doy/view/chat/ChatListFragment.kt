package com.napptilians.doy.view.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatListItemModel
import com.napptilians.domain.models.chat.ChatRequestModel
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

        viewModel.getChats()

        // SingleLiveEvent Observers
        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<ChatRequestModel, ErrorModel>> { status ->
                handleUiStates(status) { processTargetUser(it) }
            }
        )
        viewModel.chatListDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Map<String, List<ChatListItemModel>>, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )
        viewModel.chatUpdateDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<ChatListItemModel, ErrorModel>> {
                handleUiStates(it) { response -> processNewMessage(response) }
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
                getString(R.string.tab_active)
            )
            addFragment(
                ChatTabFragment { navigateToChat(it) },
                getString(R.string.tab_past)
            )
        }
        chatsViewPager.adapter = chatsAdapter
        chatsViewPager.offscreenPageLimit = 3
        chatsTabLayout.setupWithViewPager(chatsViewPager)
    }

    override fun onError(error: ErrorModel) {
        chatsViewPager.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
    }

    private fun processNewValue(model: Map<String, List<ChatListItemModel>>) {
        chatsViewPager.visible()
        (chatsAdapter.getItem(0) as ChatTabFragment).setItems(
            model[ChatListItemModel.UPCOMING] ?: listOf()
        )
        (chatsAdapter.getItem(1) as ChatTabFragment).setItems(
            model[ChatListItemModel.ACTIVE] ?: listOf()
        )
        (chatsAdapter.getItem(2) as ChatTabFragment).apply {
            setItems(model[ChatListItemModel.PAST] ?: listOf())
        }
    }

    private fun processNewMessage(model: ChatListItemModel) {
        when (model.status) {
            ChatListItemModel.Status.Upcoming -> {
                (chatsAdapter.getItem(0) as ChatTabFragment).updateSingleItem(
                    model
                )
            }
            ChatListItemModel.Status.Active -> {
                (chatsAdapter.getItem(1) as ChatTabFragment).updateSingleItem(
                    model
                )
            }
            ChatListItemModel.Status.Past -> {
                (chatsAdapter.getItem(2) as ChatTabFragment).updateSingleItem(
                    model
                )
            }
            ChatListItemModel.Status.Unknown -> { }
        }
    }

    private fun processTargetUser(requestModel: ChatRequestModel) {
        val direction = ChatListFragmentDirections.actionChatListFragmentToChatFragment(
            requestModel
        )
        findNavController().navigate(direction)
    }

    private fun navigateToChat(chatUiModel: ChatListItemModel) {
        viewModel.getChatInformation(
            chatUiModel.id,
            chatUiModel.name,
            chatUiModel.date,
            chatUiModel.duration
        )
    }
}
