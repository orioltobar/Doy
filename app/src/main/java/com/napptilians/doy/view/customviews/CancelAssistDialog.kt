package com.napptilians.doy.view.customviews

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.Window
import android.widget.LinearLayout
import com.napptilians.doy.R
import kotlinx.android.synthetic.main.cancel_assist_dialog.cancelAssistanceButton

class CancelAssistDialog(context: Context, onCancelAssist: () -> Unit) : Dialog(context, R.style.Theme_Design_BottomSheetDialog) {

    init {
        super.onStart()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.setContentView(R.layout.cancel_assist_dialog)
        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
        cancelAssistanceButton.setOnClickListener {
            onCancelAssist.invoke()
            dismiss()
        }

        window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val attributes = window?.attributes?.apply {
            gravity = Gravity.BOTTOM
        }
        window?.attributes = attributes
    }
}
