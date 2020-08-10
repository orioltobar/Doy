package com.napptilians.doy.view.user

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.user.UserModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.extensions.clickable
import com.napptilians.doy.extensions.encodeByteArrayToBase64
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.notClickable
import com.napptilians.doy.extensions.resize
import com.napptilians.doy.extensions.toByteArray
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.profile_fragment.profileFragmentProgressView
import kotlinx.android.synthetic.main.profile_fragment.profileImageCardView
import kotlinx.android.synthetic.main.profile_fragment.profileImageView
import kotlinx.android.synthetic.main.profile_fragment.profileInfoFrameLayout
import kotlinx.android.synthetic.main.profile_fragment.profileInfoLogOutText
import kotlinx.android.synthetic.main.profile_fragment.profileInfoSaveChangesButton
import kotlinx.android.synthetic.main.profile_fragment.profilePhotoPlaceHolder
import kotlinx.android.synthetic.main.profile_fragment.profileTitle
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ToolbarBehaviour {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override val genericToolbar: Toolbar?
        get() = activity?.findViewById(R.id.toolbar)

    private var editAction: MenuItem? = null

    // TODO: Re-do the logic after MVP

    private var isEditMode: Boolean = false
    private var isEditingImage: Boolean = false

    private val viewModel: ProfileViewModel by viewModels { vmFactory }

    private var editModeView: ProfileEditView? = null
    private var readModeView: ProfileReadView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genericToolbar?.let {
            it.menu.clear()
            it.inflateMenu(R.menu.profile_menu)
            editAction = it.menu.findItem(R.id.menuEdit)
            it.navigationIcon = null
        }

        editAction?.setOnMenuItemClickListener {
            switchEditMode()
            return@setOnMenuItemClickListener false
        }
        // Show read view as default.
        defaultView()

        profilePhotoPlaceHolder.setOnClickListener {
            openGallery()
        }

        profileInfoLogOutText.setOnClickListener {
            viewModel.logout()
        }

        profileInfoSaveChangesButton.setOnClickListener {
            editModeView?.let {
                val name = it.getUserName()
                val description = it.getDescription()
                viewModel.updateUser(name = name, description = description)
                profileInfoSaveChangesButton.notClickable()
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

    private fun openGallery() {
        activity?.let {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    private fun processNewValue(user: UserModel) {
        profileFragmentProgressView.gone()
        profileInfoSaveChangesButton.clickable()
        if (user.image.isNotEmpty()) {
            profileImageCardView.visible()
            Glide.with(profileImageView)
                .load(user.image)
                .placeholder(profileImageView?.drawable)
                .into(profileImageView)
        } else {
            profileImageCardView.gone()
        }
        if (isEditMode && !isEditingImage) {
            switchEditMode(user)
        } else {
            readModeView?.apply {
                updateFields(user)
                profileInfoFrameLayout.removeAllViews()
                profileInfoFrameLayout.addView(this)
            }
        }
        isEditingImage = false
    }

    override fun onError(error: ErrorModel) {
        profileFragmentProgressView.gone()
        val errorString = error.message
            ?.takeIf { it.isNotEmpty() }
            ?: run { getString(R.string.error_message) }

        Toast.makeText(activity, errorString, Toast.LENGTH_LONG).show()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        profileFragmentProgressView.visible()
    }

    private fun logout() {
        val direction = ProfileFragmentDirections.actionProfileFragmentToIntroFragment()
        findNavController().navigate(direction)
    }

    private fun navigateToMyEvents() {
        firebaseAuth.currentUser?.let {
            val navigation =
                ProfileFragmentDirections.actionProfileFragmentToEventsFragment(it.uid, true)
            findNavController().navigate(navigation)
        }
    }

    private fun defaultView() {
        context?.let {
            readModeView = ProfileReadView(
                it,
                onAddDescriptionClicked = { switchEditMode() },
                onMyEventsClicked = { navigateToMyEvents() })
            editModeView = ProfileEditView(it)
            profileInfoFrameLayout.addView(readModeView)
        }
    }

    private fun switchEditMode(user: UserModel? = null) {
        context?.let {
            if (!isEditMode) {
                editAction?.isVisible = false
                enableHomeAsUp { switchEditMode() }
                profileTitle.text = getString(R.string.edit_profile)
                profileInfoLogOutText.gone()
                profileInfoSaveChangesButton.visible()
                editModeView?.apply {
                    setUserMail(viewModel.getUserEmail())
                    setUserName(viewModel.getUserName())
                    if (viewModel.getUserDescription().isNotEmpty()) {
                        setUserDescription(viewModel.getUserDescription())
                    } else {
                        setFocusOnUserDescription()
                    }
                    profileInfoFrameLayout.removeAllViews()
                    profileInfoFrameLayout.addView(this)
                    isEditMode = true
                }

            } else {
                editAction?.isVisible = true
                genericToolbar?.navigationIcon = null
                profileTitle.text = getString(R.string.profile)
                profileInfoLogOutText.visible()
                profileInfoSaveChangesButton.gone()
                readModeView?.apply {
                    user?.let { updateFields(it) }
                    profileInfoFrameLayout.removeAllViews()
                    profileInfoFrameLayout.addView(this)
                    isEditMode = false
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                data?.data?.let { imageUri ->
                    val imageBitmap = BitmapFactory.decodeStream(
                        context?.contentResolver?.openInputStream(imageUri)
                    )?.resize()
                    imageBitmap?.let {
                        profileImageCardView.visible()
                        profileImageView.setImageBitmap(it)
                        viewModel.updateUser(image = it.toByteArray()?.encodeByteArrayToBase64())
                        isEditingImage = true
                    }
                }
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 101
    }
}
