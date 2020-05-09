package com.napptilians.doy.view.user

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.napptilians.doy.R
import kotlinx.android.synthetic.main.profile_fragment_edit_mode.view.profileDescriptionEditText
import kotlinx.android.synthetic.main.profile_fragment_edit_mode.view.profileEmailEditModeEditText
import kotlinx.android.synthetic.main.profile_fragment_edit_mode.view.profileNameEditText

class ProfileEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.profile_fragment_edit_mode, this, true)
        profileDescriptionEditText.setOnFocusChangeListener { _, _ ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    fun setUserMail(mail: String) {
        profileEmailEditModeEditText.setText(mail)
    }

    fun setUserName(name: String) {
        profileNameEditText.setText(name)
    }

    fun setUserDescription(description: String) {
        profileDescriptionEditText.setText(description)
    }

    fun setFocusOnUserDescription() {
        profileDescriptionEditText.requestFocus()
    }

    fun getUserName(): String = profileNameEditText.text.toString()

    fun getDescription(): String = profileDescriptionEditText.text.toString()
}