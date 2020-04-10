package com.napptilians.doy.view.selectduration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.SelectDurationViewModel
import kotlinx.android.synthetic.main.select_duration_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectDurationFragment : BaseFragment() {

    private val viewModel: SelectDurationViewModel by viewModels { vmFactory }

    @Inject
    lateinit var durationListAdapter: DurationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.select_duration_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.execute()
        viewModel.durationsDataStream.observe(viewLifecycleOwner,
            Observer<UiStatus<List<Int>, ErrorModel>> { handleUiStates(it, ::processNewValue) }
        )
    }

    private fun processNewValue(durations: List<Int>) {
        val layoutManager = LinearLayoutManager(context)
        durationList.layoutManager = layoutManager
        durationListAdapter = DurationListAdapter()
        durationList.adapter = durationListAdapter
        durationList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        durationListAdapter.updateItems(durations)
    }

    override fun onError(error: ErrorModel) {
        // Do nothing
    }

    override fun onLoading() {
        // Do nothing
    }
}
