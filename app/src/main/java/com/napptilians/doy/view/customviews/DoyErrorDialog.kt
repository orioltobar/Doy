package com.napptilians.doy.view.customviews

import android.content.Context
import com.napptilians.doy.R

class DoyErrorDialog(context: Context) : DoyDialog(context) {

    init {
        setPopupIcon(R.drawable.ic_error_sad)
        setPopupTitle(context.resources.getString(R.string.error_title))
        setPopupSubtitle(context.resources.getString(R.string.error_message))
    }
}
