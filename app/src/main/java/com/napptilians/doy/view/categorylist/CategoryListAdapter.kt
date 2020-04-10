package com.napptilians.doy.view.categorylist

import android.view.ViewGroup
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class CategoryListAdapter @Inject constructor() : BaseAdapter<CategoryModel, CategoryItemViewHolder>() {

    var isAddingService: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(parent, isAddingService)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}
