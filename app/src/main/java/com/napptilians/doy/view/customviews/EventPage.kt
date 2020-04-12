package com.napptilians.doy.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.servicelist.ServiceListAdapter
import kotlinx.android.synthetic.main.event_page.view.eventsList
import kotlinx.android.synthetic.main.event_page.view.eventsLoadingProgress
import kotlinx.android.synthetic.main.event_page.view.eventsLoadingText

class EventPage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var eventsAdapter: ServiceListAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.event_page, this, true)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.recycleChildrenOnDetach = true
        eventsList.layoutManager = layoutManager
        eventsAdapter = ServiceListAdapter()
        eventsAdapter.setOnClickListener {
            println("Event clicked!")
        }
        eventsList.adapter = eventsAdapter
    }

    fun setItems(events: List<ServiceModel>) {
        eventsList.visible()
        eventsAdapter.updateItems(events)
        eventsLoadingText.gone()
        eventsLoadingProgress.gone()
    }
}