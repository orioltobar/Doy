package com.napptilians.doy.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.LoginErrors
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.LoginViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.register_fragment.registerFragmentEmailField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentNameField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentPasswordField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentRepeatPasswordField

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpText.setOnClickListener {
            // Navigate to Sign Up fragment
            val direction = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(direction)
        }
        recoverPassText.setOnClickListener {
            // Navigate to Recover pass fragment
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
        loginFragmentProgressView.gone()
        when  {
            error.errorCause == LoginErrors.InvalidEmail -> {
                setErrorFields(loginFragmentEmailField, R.string.invalid_email)
                resetField(loginFragmentPasswordField)
            }
            error.errorCause == LoginErrors.EmptyPassword -> {
                resetField(loginFragmentEmailField)
                setErrorFields(loginFragmentPasswordField, R.string.empty_password)
            }
            error.errorCause == null && error.errorMessage!= null -> {
                Toast.makeText(activity, error.errorMessage, Toast.LENGTH_LONG)
                    .show()
                firebaseAuth.signOut()
            }
            else -> {
                Toast.makeText(activity, getString(R.string.error_message), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setErrorFields(view: TextInputLayout, messageIdRes: Int) {
        view.apply {
            helperText = getString(messageIdRes)
            setHintTextAppearance(R.style.ErrorTheme)
            setErrorTextAppearance(R.style.ErrorTheme)
            error = getString(messageIdRes)
            setErrorIconDrawable(R.drawable.ic_error)
        }
    }

    private fun resetField(view: TextInputLayout) {
        view.apply {
            helperText = ""
            setHintTextAppearance(0)
            setErrorTextAppearance(0)
            error = ""
            setErrorIconDrawable(0)
        }
    }

    override fun onLoading() {
        loginFragmentProgressView.visible()
    }

    private fun processNewValue(auth: AuthResult) {
        loginFragmentProgressView.visible()
        val direction = LoginFragmentDirections.actionLoginFragmentToMenuFavouritesListButton()
        findNavController().navigate(direction)
    }

    private fun sendData() {
        disableLoginButton()
        val email = loginFragmentEmailEditText.text.toString().replace(" ", "")
        val password = loginFragmentPasswordEditText.text.toString()
        viewModel.login(email, password)
    }

    private fun disableLoginButton() {
        signInButton.isClickable = false
    }

    private fun enableLoginButton() {
        signInButton.isClickable = true
    }
}
