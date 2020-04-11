package com.napptilians.doy.view.events

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.view.servicelist.ServiceListAdapter

class EventsPagerAdapter(val context: Context) : PagerAdapter() {

    private val eventsMap = mapOf<String, List<ServiceModel>>()
    private lateinit var eventsList: RecyclerView
    private lateinit var eventsAdapter: ServiceListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = eventsMap.keys.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.addView(initView())
    }

    private fun initView(): View {
        eventsAdapter = ServiceListAdapter()
        eventsList = RecyclerView(context)
        layoutManager = LinearLayoutManager(context)
        layoutManager.recycleChildrenOnDetach = true
        eventsList.layoutManager = layoutManager
        eventsList.adapter = eventsAdapter
        return eventsList
    }

}