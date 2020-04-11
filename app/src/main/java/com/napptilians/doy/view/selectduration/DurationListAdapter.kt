package com.napptilians.doy.view.selectduration

import android.view.ViewGroup
import com.napptilians.domain.models.movie.DurationModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class DurationListAdapter @Inject constructor() : BaseAdapter<DurationModel, DurationListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DurationListViewHolder {
        return DurationListViewHolder(parent)
    }
}
