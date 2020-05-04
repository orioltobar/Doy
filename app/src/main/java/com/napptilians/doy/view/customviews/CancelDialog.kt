package com.napptilians.doy.view.customviews

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.widget.LinearLayout
import com.napptilians.doy.R
import kotlinx.android.synthetic.main.cancel_dialog.cancelDialogButton
import kotlinx.android.synthetic.main.cancel_dialog.cancelDialogTitle

class CancelDialog(context: Context, onCancel: () -> Unit) : Dialog(context, R.style.Theme_Design_BottomSheetDialog) {

    init {
        super.onStart()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.cancel_dialog)
        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
        cancelDialogButton.setOnClickListener {
            onCancel.invoke()
            dismiss()
        }

        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val attributes = window?.attributes?.apply {
            gravity = Gravity.BOTTOM
        }
        window?.attributes = attributes
    }

    fun setTitle(title: String) {
        cancelDialogTitle.text = title
    }

    fun setButtonText(buttonText: String) {
        cancelDialogButton.text = buttonText
    }
}
