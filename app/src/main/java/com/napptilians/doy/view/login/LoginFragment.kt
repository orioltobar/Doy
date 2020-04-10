package com.napptilians.doy.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels { vmFactory }

    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText.addTextChangedListener(emailTextWatcher)
        passwordEditText.addTextChangedListener(passwordTextWatcher)
        signUpText.setOnClickListener {
            // Navigate to Sign Up fragment
            val direction = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(direction)
        }

        signInButton.setOnClickListener {
            sendData()
        }

        // LiveData Observer
        viewModel.loginDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<AuthResult, ErrorModel>> {
                handleUiStates(it) { auth ->
                    processNewValue(
                        auth
                    )
                }
            }
        )
    }

    override fun onError(error: ErrorModel) {
        enableLoginButton()
        loginFragmentProgressView.visibility = View.GONE
        Toast.makeText(activity, error.errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onLoading() {
        loginFragmentProgressView.visibility = View.VISIBLE
    }

    private fun processNewValue(auth: AuthResult) {
        loginFragmentProgressView.visibility = View.GONE
        val direction = LoginFragmentDirections.actionLoginFragmentToMenuFavouritesListButton()
        findNavController().navigate(direction)
    }

    private fun sendData() {
        disableLoginButton()
        val email = emailEditText.text.toString().replace(" ", "")
        val password = passwordEditText.text.toString()
        if (password.isEmpty() || email.isEmpty()) {
            Toast.makeText(activity, R.string.wrong_credentials, Toast.LENGTH_LONG).show()
        } else {
            viewModel.login(email, password)
        }
    }

    private fun disableLoginButton() {
        signInButton.isClickable = false
    }

    private fun enableLoginButton() {
        signInButton.isClickable = true
    }
}
