package com.napptilians.doy.view.servicelist

import android.util.TypedValue
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.service_item.view.serviceDateText
import kotlinx.android.synthetic.main.service_item.view.serviceDurationText
import kotlinx.android.synthetic.main.service_item.view.serviceImage
import kotlinx.android.synthetic.main.service_item.view.serviceMaxSpotsText
import kotlinx.android.synthetic.main.service_item.view.serviceNameText
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


class ServiceItemViewHolder(parent: ViewGroup, private val isPastService: Boolean) :
    BaseViewHolder<ServiceModel>(parent, R.layout.service_item) {

    override fun update(model: ServiceModel) {
        with(model) {
            if (isPastService) {
                itemView.setBackgroundResource(0)
            } else {
                val outValue = TypedValue()
                itemView.setBackgroundResource(outValue.resourceId)
            }
            setImage(this)
            setName(this)
            setDate(this)
            setMaxSpots(this)
            setDuration(this)
        }
    }

    private fun setImage(model: ServiceModel) {
        itemView.serviceImage.clipToOutline = true
        Glide.with(itemView)
            .load(model.image)
            .placeholder(R.drawable.ic_logo_colour_green)
            .into(itemView.serviceImage)
    }

    private fun setName(model: ServiceModel) {
        if (model.name.isNullOrEmpty()) {
            return
        }
        itemView.serviceNameText.text = model.name
    }

    private fun setDate(model: ServiceModel) {
        if (model.day.isNullOrEmpty() || model.date == null) {
            return
        }
        val formatterUserFriendly = DateTimeFormatter.ofPattern(
            DATE_FORMAT_USER,
            Locale(Locale.getDefault().language, Locale.getDefault().country)
        )
        itemView.serviceDateText.text = formatterUserFriendly.format(model.date).capitalize()
        itemView.serviceDateText.visible()
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

    companion object {
        private const val DATE_FORMAT_USER = "EEEE d MMM, k:mm"
    }
}
