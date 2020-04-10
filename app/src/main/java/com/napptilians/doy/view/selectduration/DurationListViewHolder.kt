package com.napptilians.doy.view.selectduration

import android.view.ViewGroup
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseViewHolder
import kotlinx.android.synthetic.main.duration_item.view.*

class DurationListViewHolder(private val parent: ViewGroup) :
    BaseViewHolder<Int>(parent, R.layout.duration_item) {

    override fun update(model: Int) {
        itemView.durationText.text = model.toString()
        if (model == 1) {
            itemView.durationText.append(" hora")
        } else {
            itemView.durationText.append(" horas")
        }
    }
}
