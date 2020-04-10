package com.napptilians.doy.view.selectspots

import android.view.ViewGroup
import com.napptilians.domain.models.movie.SpotsModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class SpotListAdapter @Inject constructor() : BaseAdapter<SpotsModel, SpotListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotListViewHolder {
        return SpotListViewHolder(parent)
    }
}
