package com.napptilians.doy.view.categorylist

import android.view.ViewGroup
import com.napptilians.domain.models.category.Category
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class CategoryListAdapter @Inject constructor(): BaseAdapter<Category, CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(parent)
    }
}
