package com.napptilians.doy.view.selectspots

import android.view.ViewGroup
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.spot_item.view.*

class SpotListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<Int>(parent, R.layout.spot_item) {

    override fun update(model: Int) {
        itemView.spotText.text = model.toString()
        if (model == 1) {
            itemView.spotText.append(" " + parent.context.getString(R.string.person))
        } else {
            itemView.spotText.append(" " + parent.context.getString(R.string.persons))
        }
    }
}
