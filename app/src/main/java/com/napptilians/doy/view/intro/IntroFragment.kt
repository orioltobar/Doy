package com.napptilians.doy.view.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import kotlinx.android.synthetic.main.intro_fragment.continueGoogleButton
import kotlinx.android.synthetic.main.intro_fragment.introFragmentProgressView
import kotlinx.android.synthetic.main.intro_fragment.signInButton
import kotlinx.android.synthetic.main.intro_fragment.signUpButton
import javax.inject.Inject

class IntroFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

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
        continueGoogleButton.setOnClickListener { signInWithGoogle() }
    }

    override fun onError(error: ErrorModel) {
        // Do nothing
    }

    override fun onLoading() {
        // Do nothing
    }

    private fun configureGoogleSignIn() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        activity?.let { googleSignInClient = GoogleSignIn.getClient(it, googleSignInOptions) }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                (task.getResult(ApiException::class.java))?.run {
                    Log.d(TAG, "Firebase Auth with Google: $id")
                    firebaseAuthWithGoogle(idToken ?: "")
                }
            } catch (e: ApiException) {
                // Google Sign In failed, display a message to the user
                Log.w(TAG, "Google sign in failed", e)
                onGoogleSignInError("Google Sign In failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        introFragmentProgressView.visible()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Sign In with credentials: Success")
                    val user = firebaseAuth.currentUser
                    onGoogleSignInSuccess(user)
                } else {
                    // Sign in fail, display a message to the user
                    Log.w(TAG, "Sign In with credentials: Failure", task.exception)
                    onGoogleSignInError("Authentication Failed")
                }
                introFragmentProgressView.gone()
            }
    }

    private fun onGoogleSignInError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun onGoogleSignInSuccess(user: FirebaseUser?) {
        user?.let {
            // TODO: Save credentials and Log in
            Toast.makeText(context, "Hello ${it.displayName}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "IntroFragment"
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}
