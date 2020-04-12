package com.napptilians.doy.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import javax.inject.Inject

class SplashFragment : BaseFragment() {

    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.splash_screen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user is logged.
        val direction = if (fireBaseAuth.currentUser == null) {
            SplashFragmentDirections.actionSplashFragmentToIntroFragment()
        } else {
            SplashFragmentDirections.actionSplashFragmentToCategoryListFragment()
        }
        findNavController().navigate(direction)
    }

    override fun onError(error: ErrorModel) {

    }

    override fun onLoading() {

    }
}