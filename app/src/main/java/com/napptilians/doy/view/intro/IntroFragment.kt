package com.napptilians.doy.view.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.IntroViewModel
import kotlinx.android.synthetic.main.intro_fragment.continueGoogleButton
import kotlinx.android.synthetic.main.intro_fragment.introFragmentProgressView
import kotlinx.android.synthetic.main.intro_fragment.signInButton
import kotlinx.android.synthetic.main.intro_fragment.signUpButton
import javax.inject.Inject

class IntroFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: IntroViewModel by viewModels { vmFactory }

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureGoogleSignIn()
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

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
        continueGoogleButton.setOnClickListener { chooseGoogleSignInAccount() }

        // LiveData Observer
        viewModel.loginWithGoogleDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<String, ErrorModel>> {
                handleUiStates(it) { unit ->
                    processNewValue(unit)
                }
            }
        )
    }

    override fun onError(error: ErrorModel) {
        introFragmentProgressView.gone()
        showMessage(error.message ?: "")
    }

    override fun onLoading() {
        introFragmentProgressView.visible()
    }

    private fun processNewValue(userName: String) {
        introFragmentProgressView.gone()
        val direction = IntroFragmentDirections.actionIntroFragmentToCategoryListFragment()
        findNavController().navigate(direction)
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        activity?.let { googleSignInClient = GoogleSignIn.getClient(it, googleSignInOptions) }
    }

    private fun chooseGoogleSignInAccount() {
        // Clear previous selection (if any)
        googleSignInClient.signOut()
        // Start google sign in activity
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                (task.getResult(ApiException::class.java))?.run {
                    // Google Sign In was successful, authenticate with Firebase
                    loginWithGoogle(idToken ?: "")

                }
            } catch (e: ApiException) {
                // Google Sign In failed, display a message to the user
                showMessage(getString(R.string.google_sign_in_failed))
            }
        }
    }

    private fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModel.login(credential)
    }

    private fun showMessage(message: String) {
        view?.let {
            if (message.isNotEmpty()) {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}
