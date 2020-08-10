package com.napptilians.doy.view.selectduration

import android.view.ViewGroup
import com.napptilians.domain.models.service.DurationModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.HourFormatter
import kotlinx.android.synthetic.main.duration_item.view.*

class DurationListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<DurationModel>(parent, R.layout.duration_item) {

    private val hourFormatter = HourFormatter()

    override fun update(model: DurationModel) {
        itemView.durationText.text = hourFormatter.formatHour(parent.context, model.numberOfMinutes)
        if (model.isSelected) {
            itemView.tickImageView.visible()
            itemView.durationText.alpha = 1f
        } else {
            itemView.tickImageView.gone()
            if (model.shouldBeSelected) {
                itemView.durationText.alpha = 1f
            } else {
                itemView.durationText.alpha = 0.5f
            }
        }
    }
}
