package com.napptilians.doy.view.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.EventsModel
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.EventsViewModel
import com.napptilians.features.viewmodel.ServicesViewModel
import kotlinx.android.synthetic.main.events_fragment.eventsTabLayout
import kotlinx.android.synthetic.main.events_fragment.eventsViewPager
import kotlinx.android.synthetic.main.events_fragment.titleText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.Instant
import java.time.ZoneId

@ExperimentalCoroutinesApi
class EventsFragment : BaseFragment() {

    private val viewModel: ServicesViewModel by viewModels { vmFactory }

    private val user = "123123123oajsdoj"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.events_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.execute(uid = user)
        viewModel.servicesDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<ServiceModel>, ErrorModel>> {
                handleUiStates(
                    it,
                    ::processNewValue
                )
            })
    }

    override fun onError(error: ErrorModel) {
    }

    override fun onLoading() {
    }

    private fun processNewValue(model: List<ServiceModel>) {
        // TODO: This should not be handled by the view, but time constraints happen.
        val h = model.groupBy { it.date?.let { date -> date < Instant.now().atZone(ZoneId.of("Europe/Madrid")) }}
        Toast.makeText(context, "Lhola", Toast.LENGTH_SHORT).show()
    }

    private fun initViews() {
        titleText.visible()

        //eventsViewPager.adapter = FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        eventsTabLayout.setupWithViewPager(eventsViewPager)
    }

}
