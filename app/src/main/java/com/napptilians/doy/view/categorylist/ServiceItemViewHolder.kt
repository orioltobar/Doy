package com.napptilians.doy.view.categorylist

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.movie.CategoryModel
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.category_item.view.*

class ServiceItemViewHolder(parent: ViewGroup) :
    BaseViewHolder<ServiceModel>(parent, R.layout.category_item) {

    override fun update(model: ServiceModel) {
        itemView.categoryText.text = model.name
        itemView.categoryImage.clipToOutline = true
        Glide.with(itemView)
            .load(model.pictureUrl)
            .placeholder(R.color.colorAccent)
            .into(itemView.categoryImage)
    }
}
