package com.napptilians.doy.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.view.customviews.DoyDialog
import kotlinx.android.synthetic.main.intro_fragment.continueGoogleButton
import kotlinx.android.synthetic.main.intro_fragment.signInButton
import kotlinx.android.synthetic.main.intro_fragment.signUpButton

class IntroFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.intro_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpButton.setOnClickListener {
            // Navigate to Sign Up fragment
            val direction = IntroFragmentDirections.actionIntroFragmentToRegisterFragment()
            findNavController().navigate(direction)
        }
        signInButton.setOnClickListener {
            // Navigate to Sign Up fragment
            val direction = IntroFragmentDirections.actionIntroFragmentToLoginFragment()
            findNavController().navigate(direction)
        }
        continueGoogleButton.setOnClickListener {
            // Authenticate with Google
            activity?.let { activity ->
                DoyDialog(activity).apply {
                    setPopupIcon(R.drawable.ic_rocket)
                    setPopupTitle(context.resources.getString(R.string.wip))
                    setPopupSubtitle(context.resources.getString(R.string.wip_explanation))
                    show()
                }
            }
        }
    }

    override fun onError(error: ErrorModel) {
        // Do nothing
    }

    override fun onLoading() {
        // Do nothing
    }
}
