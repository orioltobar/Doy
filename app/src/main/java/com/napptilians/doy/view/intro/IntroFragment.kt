package com.napptilians.doy.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.intro_fragment.*

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
        }
        signInButton.setOnClickListener {
            // Navigate to Sign Up fragment
        }
        continueGoogleButton.setOnClickListener {
            // Authenticate with Google
        }
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
