package com.napptilians.doy.view.selectduration

import android.view.ViewGroup
import com.napptilians.domain.models.service.DurationModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.duration_item.view.durationText
import kotlinx.android.synthetic.main.duration_item.view.tickImageView

class DurationListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<DurationModel>(parent, R.layout.duration_item) {

    override fun update(model: DurationModel) {
        itemView.durationText.text = buildDurationString(model.numberOfMinutes)
        if (model.isSelected) {
            itemView.tickImageView.visible()
            itemView.durationText.alpha = 1f
        } else {
            itemView.tickImageView.gone()
            itemView.durationText.alpha = 0.5f
        }
    }

    private fun buildDurationString(numberOfMinutes: Int): String {
        val hours = numberOfMinutes / 60
        val minutes = numberOfMinutes % 60
        return when {
            hours == 0 -> {
                parent.context.resources.getString(R.string.minutes, minutes)
            }
            numberOfMinutes == 60 -> {
                parent.context.resources.getQuantityString(
                    R.plurals.hours,
                    hours,
                    hours
                )
            }
            hours < 2 -> {
                parent.context.resources.getString(R.string.hours_and_minutes, hours, minutes)
            }
            hours == 2 && minutes == 0 -> {
                parent.context.resources.getQuantityString(
                    R.plurals.hours,
                    hours,
                    hours
                )
            }
            else -> {
                "+ " + parent.context.resources.getQuantityString(
                    R.plurals.hours,
                    hours,
                    hours
                )
            }
        }
    }
}
