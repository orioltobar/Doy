package com.napptilians.doy.view.user

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.napptilians.domain.models.user.UserModel
import com.napptilians.doy.R
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileBioPlaceHolderDefaultText
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileBioPlaceHolderText
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileEventsReadMode
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileEventsReadModeContainer
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileNameReadModeName
import kotlinx.android.synthetic.main.profile_fragment_read_mode.view.profileNameReadModeShape

class ProfileReadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    onMyEventsClicked: () -> Unit = {}
 ) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.profile_fragment_read_mode, this, true)

        // Events
        profileEventsReadModeContainer.setOnClickListener {
            onMyEventsClicked()
        }
    }

    fun updateFields(user: UserModel) {
        // Profile Image
        // TODO: Pending.

        // Name
        profileNameReadModeName.text = user.name

        // Description
        if (user.description.isEmpty()) {
            profileNameReadModeShape.visible()
            profileBioPlaceHolderDefaultText.visible()
            profileBioPlaceHolderText.gone()
        } else {
            profileNameReadModeShape.gone()
            profileBioPlaceHolderDefaultText.gone()
            profileBioPlaceHolderText.visible()
            profileBioPlaceHolderText.text = user.description
        }
    }
}