package com.napptilians.doy.view.categorylist

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(val context: Context?, @DimenRes val itemOffsetId: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        context?.let {
            val itemOffset = context.resources.getDimensionPixelSize(itemOffsetId)
            outRect.set(itemOffset, itemOffset, itemOffset, itemOffset);
        }
    }
}
