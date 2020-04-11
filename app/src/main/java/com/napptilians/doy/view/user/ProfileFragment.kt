package com.napptilians.doy.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.user.UserModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.profileEditMode
import kotlinx.android.synthetic.main.profile_fragment.profileFragmentProgressView
import kotlinx.android.synthetic.main.profile_fragment.profileInfoFrameLayout
import kotlinx.android.synthetic.main.profile_fragment.profileInfoLogOutText
import kotlinx.android.synthetic.main.profile_fragment.profileInfoSaveChangesButton
import kotlinx.android.synthetic.main.profile_fragment.profileTitle

class ProfileFragment : BaseFragment() {

    private var isEditMode: Boolean = false

    private val viewModel: ProfileViewModel by viewModels { vmFactory }

    private val editModeView: ProfileEditView? by lazy {
        context?.let { ProfileEditView(it) }
    }

    private val readModeView: ProfileReadView? by lazy {
        context?.let { ProfileReadView(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show read view as default.
        defaultView()

        profileEditMode.setOnClickListener {
            switchEditMode()
        }

        profileInfoLogOutText.setOnClickListener {
            viewModel.logout()
        }

        profileInfoSaveChangesButton.setOnClickListener {
            editModeView?.let {
                val name = it.getUserName()
                val description = it.getDescription()
                viewModel.updateUser(name = name, description = description)
            }
        }

        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<UserModel, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )

        viewModel.updateUserDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<UserModel, ErrorModel>> {
                handleUiStates(it, ::processNewValue)
            }
        )

        viewModel.logoutDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Unit, ErrorModel>> {
                handleUiStates(it) { logout() }
            }
        )
    }

    private fun processNewValue(user: UserModel) {
        profileFragmentProgressView.gone()
        if (isEditMode) {
            switchEditMode()
        }
        readModeView?.apply {
            updateFields(user)
            profileInfoFrameLayout.removeAllViews()
            profileInfoFrameLayout.addView(this)
        }
    }

    override fun onError(error: ErrorModel) {
        profileFragmentProgressView.gone()
        val errorString = error.message
            ?.takeIf { it.isNotEmpty() }
            ?.let { it }
            ?: run { getString(R.string.generic_error) }

        Toast.makeText(activity, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onLoading() {
        profileFragmentProgressView.visible()
    }

    private fun logout() {
        val direction = ProfileFragmentDirections.actionProfileFragmentToIntroFragment()
        findNavController().navigate(direction)
    }

    private fun defaultView() {
        context?.let {
            profileInfoFrameLayout.removeAllViews()
            profileInfoFrameLayout.addView(readModeView)
        }
    }

    private fun switchEditMode() {
        context?.let {
            if (!isEditMode) {
                profileTitle.text = getString(R.string.edit_profile)
                profileInfoLogOutText.gone()
                profileInfoSaveChangesButton.visible()
                val editView = ProfileEditView(it)
                editView.setUserMail(viewModel.getUserEmail())
                profileInfoFrameLayout.removeAllViews()
                profileInfoFrameLayout.addView(editView)
                isEditMode = true
            } else {
                profileTitle.text = getString(R.string.profile)
                profileInfoLogOutText.visible()
                profileInfoSaveChangesButton.gone()
                val readView = ProfileReadView(it)
                profileInfoFrameLayout.removeAllViews()
                profileInfoFrameLayout.addView(readView)
                isEditMode = false
            }
        }
    }
}