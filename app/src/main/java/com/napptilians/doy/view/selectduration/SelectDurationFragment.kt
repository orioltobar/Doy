package com.napptilians.doy.view.selectduration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.DurationModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.setNavigationResult
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.SelectDurationViewModel
import kotlinx.android.synthetic.main.select_duration_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectDurationFragment : BaseFragment() {

    private val viewModel: SelectDurationViewModel by viewModels { vmFactory }
    private var selectedDurationHours: Int = 0

    @Inject
    lateinit var durationListAdapter: DurationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.select_duration_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        viewModel.execute()
        viewModel.durationsDataStream.observe(viewLifecycleOwner,
            Observer<UiStatus<List<DurationModel>, ErrorModel>> { handleUiStates(it, ::processNewValue) }
        )
    }

    private fun setupListeners() {
        saveButton.setOnClickListener {
            setNavigationResult(selectedDurationHours.toString(), "selectedDuration")
            findNavController().popBackStack()
        }
    }

    private fun processNewValue(durations: List<DurationModel>) {
        val layoutManager = LinearLayoutManager(context)
        durationList.layoutManager = layoutManager
        durationListAdapter = DurationListAdapter()
        durationList.adapter = durationListAdapter
        durationList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        durationListAdapter.updateItems(durations)
        durationListAdapter.setOnClickListener { clickedCategory ->
            saveButton.visible()
            durationListAdapter.getItems().forEachIndexed { index, durationModel ->
                if (durationModel != clickedCategory && (durationModel.isSelected || durationModel.shouldBeSelected)) {
                    durationModel.shouldBeSelected = false
                    durationModel.isSelected = false
                    durationListAdapter.notifyItemChanged(index)
                } else if (durationModel == clickedCategory && !durationModel.isSelected) {
                    selectedDurationHours = durationModel.numberOfMinutes
                    durationModel.isSelected = true
                    durationListAdapter.notifyItemChanged(index)
                    durationList.scrollToPosition(index)
                }
            }
        }
    }

    override fun onError(error: ErrorModel) {
        // Do nothing
    }

    override fun onLoading() {
        // Do nothing
    }
}
