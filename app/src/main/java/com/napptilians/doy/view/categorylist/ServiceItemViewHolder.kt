package com.napptilians.doy.view.categorylist

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceItemViewHolder(parent: ViewGroup) :
    BaseViewHolder<ServiceModel>(parent, R.layout.service_item) {

    override fun update(model: ServiceModel) {
        Glide.with(itemView)
            .load(model.pictureUrl)
            .placeholder(R.drawable.ic_logo_colour_green)
            .into(itemView.serviceImage)
        itemView.serviceNameText.text = model.name
        itemView.serviceDateText.text = model.day
        itemView.serviceMaxSpotsText.text = model.spots.toString()
        itemView.serviceNameText.text = model.duration.toString()
    }
}
