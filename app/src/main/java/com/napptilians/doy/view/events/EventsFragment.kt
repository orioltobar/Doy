package com.napptilians.doy.view.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetEventsUseCase.Companion.PAST
import com.napptilians.domain.usecases.GetEventsUseCase.Companion.UPCOMING
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.events_fragment.eventsErrorText
import kotlinx.android.synthetic.main.events_fragment.eventsTabLayout
import kotlinx.android.synthetic.main.events_fragment.eventsViewPager
import kotlinx.android.synthetic.main.events_fragment.titleText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EventsFragment : BaseFragment() {

    private val viewModel: EventsViewModel by viewModels { vmFactory }
    private lateinit var eventAdapter: EventsPagerAdapter

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onError(error: ErrorModel) {
        eventsErrorText.text = "${resources.getString(R.string.error_title)}\n${resources.getString(R.string.error_message)}"
        eventsErrorText.visible()
        eventsViewPager.gone()
    }

    override fun onLoading() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.execute(uid = firebaseAuth.currentUser?.uid)
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
        eventsErrorText.gone()
        eventsViewPager.visible()
        (eventAdapter.getItem(0) as EventTabFragment).setItems(model[UPCOMING] ?: listOf())
        (eventAdapter.getItem(1) as EventTabFragment).setItems(model[PAST] ?: listOf())
    }

    private fun initViews() {
        titleText.visible()
        eventAdapter = EventsPagerAdapter(childFragmentManager)
        eventAdapter.apply {
            addFragment(EventTabFragment(), context?.resources?.getString(R.string.tab_upcoming))
            addFragment(EventTabFragment(), context?.resources?.getString(R.string.tab_past))
        }
        eventsViewPager.adapter = eventAdapter
        eventsTabLayout.setupWithViewPager(eventsViewPager)
    }
}
