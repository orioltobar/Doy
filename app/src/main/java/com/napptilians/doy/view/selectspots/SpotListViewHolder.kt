package com.napptilians.doy.view.selectspots

import android.view.ViewGroup
import com.napptilians.domain.models.movie.SpotsModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.spot_item.view.*

class SpotListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<SpotsModel>(parent, R.layout.spot_item) {

    override fun update(model: SpotsModel) {
        itemView.spotText.text = model.numberOfSpots.toString()
        if (model.numberOfSpots == 1) {
            itemView.spotText.append(" " + parent.context.getString(R.string.person))
        } else {
            itemView.spotText.append(" " + parent.context.getString(R.string.persons))
        }
        if (model.isSelected) {
            itemView.tickImageView.visible()
            itemView.spotText.alpha = 1f
        } else {
            itemView.tickImageView.gone()
            itemView.spotText.alpha = 0.5f
        }
    }
}
