package com.napptilians.doy.view.servicelist

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.service_item.view.*

class ServiceItemViewHolder(parent: ViewGroup) :
    BaseViewHolder<ServiceModel>(parent, R.layout.service_item) {

    override fun update(model: ServiceModel) {
        with(model) {
            setImage(this)
            setName(this)
            setDate(this)
            setMaxSpots(this)
            setDuration(this)
        }
    }

    private fun setImage(model: ServiceModel) {
        Glide.with(itemView)
            .load(model.image)
            .placeholder(R.drawable.image_bg)
            .into(itemView.serviceImage)
    }

    private fun setName(model: ServiceModel) {
        if (model.name.isEmpty()) {
            return
        }
        itemView.serviceNameText.text = model.name
    }

    private fun setDate(model: ServiceModel) {
        if (model.day.isEmpty()) {
            return
        }
        itemView.serviceDateText.text = model.day
    }

    private fun setMaxSpots(model: ServiceModel) {
        itemView.serviceMaxSpotsText.gone()
        if (model.spots == 0) {
            return
        }
        itemView.serviceMaxSpotsText.text =
            itemView.resources.getString(R.string.max_spots, model.spots)
        itemView.serviceMaxSpotsText.visible()
    }

    private fun setDuration(model: ServiceModel) {
        itemView.serviceDurationText.gone()
        if (model.durationMin == 0) {
            return
        }
        itemView.serviceDurationText.text =
            itemView.resources.getString(R.string.service_duration, model.durationMin)
        itemView.serviceDurationText.visible()
    }
}
