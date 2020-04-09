package com.napptilians.doy.view.servicelist

import android.view.ViewGroup
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ServiceListAdapter @Inject constructor() : BaseAdapter<CategoryModel, CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(parent)
    }
}
