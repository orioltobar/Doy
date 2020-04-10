package com.napptilians.doy.view.customviews

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.annotation.DrawableRes
import com.napptilians.doy.R
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.generic_popup.popupFooterImage
import kotlinx.android.synthetic.main.generic_popup.popupFooterMessage
import kotlinx.android.synthetic.main.generic_popup.popupIcon
import kotlinx.android.synthetic.main.generic_popup.popupSubtitle
import kotlinx.android.synthetic.main.generic_popup.popupTitle

class DoyDialog(
    context: Context
) : Dialog(context) {

    init {
        super.onStart()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.generic_popup)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    fun setPopupIcon(@DrawableRes drawableResId: Int) {
        popupIcon.apply {
            setImageDrawable(context.resources.getDrawable(drawableResId, null))
            visible()
        }
    }

    fun setPopupTitle(title: String) {
        popupTitle.apply {
            text = title
            visible()
        }
    }

    fun setPopupSubtitle(subtitle: String) {
        popupSubtitle.apply {
            text = subtitle
            visible()
        }
    }

    fun setPopupFooterMessage(footerMessage: String) {
        popupFooterMessage.apply {
            text = footerMessage
            visible()
        }
    }

    fun setPopupFooterImage(@DrawableRes drawableResId: Int) {
        popupFooterImage.apply {
            setImageDrawable(context.resources.getDrawable(drawableResId, null))
            visible()
        }
    }
}
