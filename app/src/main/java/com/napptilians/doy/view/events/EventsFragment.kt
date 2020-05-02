package com.napptilians.doy.view.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetEventsUseCase.Companion.PAST
import com.napptilians.domain.usecases.GetEventsUseCase.Companion.UPCOMING
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.events_fragment.eventsTabLayout
import kotlinx.android.synthetic.main.events_fragment.eventsViewPager
import kotlinx.android.synthetic.main.events_fragment.titleText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EventsFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels { vmFactory }
    private val args: EventsFragmentArgs by navArgs()

    private lateinit var eventAdapter: EventsPagerAdapter

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onError(error: ErrorModel) {
        eventsViewPager.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        if (args.onlyMyEvents) {
            viewModel.getMyServices(uid = firebaseAuth.currentUser?.uid)
        } else {
            viewModel.getServices(uid = firebaseAuth.currentUser?.uid)
        }
        viewModel.eventsDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>> {
                handleUiStates(
                    it,
                    ::processNewValue
                )
            })
    }

    private fun processNewValue(model: Map<String, List<ServiceModel>>) {
        eventsViewPager.visible()
        (eventAdapter.getItem(0) as EventTabFragment).setItems(model[UPCOMING] ?: listOf())
        (eventAdapter.getItem(1) as EventTabFragment).apply {
            setItems(model[PAST] ?: listOf())
            setAlphaToPastEvents(0.4f)
        }
    }

    private fun initViews() {
        titleText.visible()
        if (args.onlyMyEvents) {
            titleText.text = context?.resources?.getText(R.string.events_by_user)
        } else {
            titleText.text = context?.resources?.getText(R.string.your_events)
        }
        eventAdapter = EventsPagerAdapter(childFragmentManager)
        eventAdapter.apply {
            addFragment(
                EventTabFragment { navigateToServiceDetail(it) },
                context?.resources?.getString(R.string.tab_upcoming)
            )
            addFragment(
                EventTabFragment(false),
                context?.resources?.getString(R.string.tab_past)
            )
        }
        eventsViewPager.adapter = eventAdapter
        eventsViewPager.offscreenPageLimit = 2
        eventsTabLayout.setupWithViewPager(eventsViewPager)
    }

    private fun navigateToServiceDetail(service: ServiceModel) {
        val navigation =
            EventsFragmentDirections.actionEventsFragmentToServiceDetailFragment2(service)
        findNavController().navigate(navigation)
    }
}
