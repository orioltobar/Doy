package com.napptilians.doy.view.categorylist

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.category.Category
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryItemViewHolder(parent: ViewGroup) :
    BaseViewHolder<CategoryModel>(parent, R.layout.category_item) {

    override fun update(model: CategoryModel) {
        itemView.categoryText.text = model.name
        itemView.categoryImage.clipToOutline = true
        Glide.with(itemView)
            .load(model.pictureUrl)
            .placeholder(R.color.colorAccent)
            .into(itemView.categoryImage)
    }
}
