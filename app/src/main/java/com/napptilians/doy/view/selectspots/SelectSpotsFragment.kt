package com.napptilians.doy.view.selectspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.SpotsModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.extensions.setNavigationResult
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.SelectSpotsViewModel
import kotlinx.android.synthetic.main.select_spots_fragment.saveButton
import kotlinx.android.synthetic.main.select_spots_fragment.spotsList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SelectSpotsFragment : BaseFragment(), ToolbarBehaviour {

    override val genericToolbar: Toolbar? by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

    private val viewModel: SelectSpotsViewModel by viewModels { vmFactory }
    private var selectedSpots: Int = 0

    @Inject
    lateinit var spotListAdapter: SpotListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.select_spots_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableHomeAsUp(true) { findNavController().popBackStack() }
        setupListeners()
        viewModel.execute()
        viewModel.spotsDataStream.observe(viewLifecycleOwner,
            Observer<UiStatus<List<SpotsModel>, ErrorModel>> {
                handleUiStates(
                    it,
                    ::processNewValue
                )
            }
        )
    }

    private fun setupListeners() {
        saveButton.setOnClickListener {
            setNavigationResult(selectedSpots.toString(), "selectedSpots")
            findNavController().popBackStack()
        }
    }

    private fun processNewValue(spots: List<SpotsModel>) {
        val layoutManager = LinearLayoutManager(context)
        spotsList.layoutManager = layoutManager
        spotListAdapter = SpotListAdapter()
        spotsList.adapter = spotListAdapter
        spotListAdapter.setOnClickListener { clickedCategory ->
            saveButton.isEnabled = true
            spotListAdapter.getItems().forEachIndexed { index, spotsModel ->
                if (spotsModel != clickedCategory && spotsModel.isSelected) {
                    spotsModel.isSelected = false
                    spotListAdapter.notifyItemChanged(index)
                } else if (spotsModel == clickedCategory && !spotsModel.isSelected) {
                    selectedSpots = spotsModel.numberOfSpots
                    spotsModel.isSelected = true
                    spotListAdapter.notifyItemChanged(index)
                }
            }
        }
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
