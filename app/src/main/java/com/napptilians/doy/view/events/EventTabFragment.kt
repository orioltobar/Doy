package com.napptilians.doy.view.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.servicelist.ServiceListAdapter
import kotlinx.android.synthetic.main.event_tab_fragment.eventsList
import kotlinx.android.synthetic.main.event_tab_fragment.eventsLoadingProgress
import kotlinx.android.synthetic.main.event_tab_fragment.eventsLoadingText
import javax.inject.Inject

class EventTabFragment(
    private val isPastService: Boolean = false,
    private val onEventClicked: (ServiceModel) -> Unit = {}
) : BaseFragment() {

    @Inject
    lateinit var eventsAdapter: ServiceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.event_tab_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun setItems(events: List<ServiceModel>) {
        if (events.isNotEmpty()) {
            eventsAdapter.updateItems(events)
            eventsList.visible()
            eventsLoadingProgress.gone()
            eventsLoadingText.gone()
        } else {
            eventsList.gone()
            eventsLoadingProgress.gone()
            eventsLoadingText.text = context?.resources?.getText(R.string.no_events)
            eventsLoadingText.visible()
        }
    }

    fun setAlphaToPastEvents(alpha: Float) {
        eventsList.alpha = alpha
    }

    override fun onError(error: ErrorModel) {}

    override fun onLoading() {}

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        eventsList.layoutManager = layoutManager
        eventsAdapter = ServiceListAdapter()
        eventsAdapter.setOnClickListener {
            onEventClicked(it)
        }
        eventsList.adapter = eventsAdapter
    }
}