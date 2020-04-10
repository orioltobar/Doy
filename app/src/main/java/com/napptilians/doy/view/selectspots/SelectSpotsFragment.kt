package com.napptilians.doy.view.selectspots

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
import com.napptilians.features.viewmodel.SelectSpotsViewModel
import kotlinx.android.synthetic.main.select_spots_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectSpotsFragment : BaseFragment() {

    private val viewModel: SelectSpotsViewModel by viewModels { vmFactory }

    @Inject
    lateinit var spotListAdapter: SpotListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.select_spots_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.execute()
        viewModel.spotsDataStream.observe(viewLifecycleOwner,
            Observer<UiStatus<List<Int>, ErrorModel>> { handleUiStates(it, ::processNewValue) }
        )
    }

    private fun processNewValue(spots: List<Int>) {
        val layoutManager = LinearLayoutManager(context)
        spotsList.layoutManager = layoutManager
        spotListAdapter = SpotListAdapter()
        spotsList.adapter = spotListAdapter
        spotsList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        spotListAdapter.updateItems(spots)
    }

    override fun onError(error: ErrorModel) {
        // Do nothing
    }

    override fun onLoading() {
        // Do nothing
    }
}
