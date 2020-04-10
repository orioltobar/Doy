package com.napptilians.doy.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.profile_fragment.profileEditMode
import kotlinx.android.synthetic.main.profile_fragment.profileInfoFrameLayout

class ProfileFragment : BaseFragment() {

    private var isEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.profile_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        defaultView()
        profileEditMode.setOnClickListener {
            switchEditMode()
        }
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun defaultView() {
        context?.let {
            val readView = ProfileReadView(it)
            profileInfoFrameLayout.removeAllViews()
            profileInfoFrameLayout.addView(readView)
        }
    }

    private fun switchEditMode() {
        context?.let {
            if (!isEditMode) {
                val readView = ProfileEditView(it)
                profileInfoFrameLayout.removeAllViews()
                profileInfoFrameLayout.addView(readView)
                isEditMode = true
            } else {
                val readView = ProfileReadView(it)
                profileInfoFrameLayout.removeAllViews()
                profileInfoFrameLayout.addView(readView)
                isEditMode = false
            }
        }
    }
}