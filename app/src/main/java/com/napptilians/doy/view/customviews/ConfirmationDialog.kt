package com.napptilians.doy.view.customviews

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.widget.LinearLayout
import com.napptilians.doy.R
import kotlinx.android.synthetic.main.confirmation_dialog.confirmDialogButton
import kotlinx.android.synthetic.main.confirmation_dialog.confirmDialogTitle

class ConfirmationDialog(context: Context, onConfirm: () -> Unit) :
    Dialog(context, R.style.Theme_Design_BottomSheetDialog) {

    init {
        super.onStart()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.confirmation_dialog)
        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
        confirmDialogButton.setOnClickListener {
            onConfirm.invoke()
            dismiss()
        }

        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val attributes = window?.attributes?.apply {
            gravity = Gravity.BOTTOM
        }
        window?.attributes = attributes
    }

    fun setTitle(title: String) {
        confirmDialogTitle.text = title
    }

    fun setButtonText(buttonText: String) {
        confirmDialogButton.text = buttonText
    }
}
