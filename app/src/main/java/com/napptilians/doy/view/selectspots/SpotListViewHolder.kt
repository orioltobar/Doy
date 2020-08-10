package com.napptilians.doy.view.selectspots

import android.view.ViewGroup
import com.napptilians.domain.models.service.SpotsModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.spot_item.view.spotText
import kotlinx.android.synthetic.main.spot_item.view.tickImageView

class SpotListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<SpotsModel>(parent, R.layout.spot_item) {

    override fun update(model: SpotsModel) {
        itemView.spotText.text = parent.context.resources.getQuantityString(
            R.plurals.people,
            model.numberOfSpots,
            model.numberOfSpots
        )
        if (model.isSelected) {
            itemView.tickImageView.visible()
            itemView.spotText.alpha = 1f
        } else {
            itemView.tickImageView.gone()
            if (model.shouldBeSelected) {
                itemView.spotText.alpha = 1f
            } else {
                itemView.spotText.alpha = 0.5f
            }
        }
    }
}
