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
import com.napptilians.domain.usecases.GetChatsUseCase
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.doy.view.events.PagerAdapter
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ChatListViewModel
import kotlinx.android.synthetic.main.chat_list_fragment.chatListTitle
import kotlinx.android.synthetic.main.chat_list_fragment.chatsTabLayout
import kotlinx.android.synthetic.main.chat_list_fragment.chatsViewPager
import kotlinx.android.synthetic.main.events_fragment.eventsViewPager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// TODO: Re do this view after MVP.
@ExperimentalCoroutinesApi
class ChatListFragment : BaseFragment() {

    // TODO: WORKAROUND FOR NAVIGATION TO CHAT VIEW. THIS IS NOT MEANT TO BE HERE!!!
    private val job = SupervisorJob()

    private val errorHandler = CoroutineExceptionHandler { _, _ -> }

    private val coroutineScope: CoroutineScope =
        CoroutineScope(job + Dispatchers.Main + errorHandler)

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
                ChatTabFragment(false) { navigateToChat(it) },
                getString(R.string.tab_past)
            )
        }
        chatsViewPager.adapter = chatsAdapter
        chatsViewPager.offscreenPageLimit = 2
        chatsTabLayout.setupWithViewPager(eventsViewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onError(error: ErrorModel) {
        chatsViewPager.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        //chatListProgressView.visible()
    }

    private fun processNewValue(model: Map<String, List<ServiceModel>>) {
        //chatListProgressView.gone()
        chatsViewPager.visible()
        (chatsAdapter.getItem(0) as ChatTabFragment).setItems(
            model[GetChatsUseCase.UPCOMING] ?: listOf()
        )
        (chatsAdapter.getItem(1) as ChatTabFragment).apply {
            setItems(model[GetChatsUseCase.PAST] ?: listOf())
            setAlphaToPastEvents(0.4f)
        }
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

    private fun navigateToChat(serviceModel: ServiceModel) {
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
}
