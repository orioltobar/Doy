package com.napptilians.doy.view.selectspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.select_spots_fragment.*
import javax.inject.Inject

class SelectSpotsFragment : BaseFragment() {

    @Inject
    lateinit var spotListAdapter: SpotListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.select_spots_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(context)
        spotsList.layoutManager = layoutManager
        spotListAdapter = SpotListAdapter()
        spotsList.adapter = spotListAdapter
        spotsList.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        spotListAdapter.updateItems((MIN_PERSONS..MAX_PERSONS).toList())
    }

    override fun onError(error: ErrorModel) {
        TODO("Not yet implemented")
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val MIN_PERSONS = 1
        private const val MAX_PERSONS = 10
    }
}
