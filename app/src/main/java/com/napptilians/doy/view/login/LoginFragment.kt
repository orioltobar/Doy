package com.napptilians.doy.view.login

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.FirebaseErrors
import com.napptilians.commons.error.LoginErrors
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.login_fragment.loginFragmentEmailEditText
import kotlinx.android.synthetic.main.login_fragment.loginFragmentEmailField
import kotlinx.android.synthetic.main.login_fragment.loginFragmentPasswordEditText
import kotlinx.android.synthetic.main.login_fragment.loginFragmentPasswordField
import kotlinx.android.synthetic.main.login_fragment.loginFragmentProgressView
import kotlinx.android.synthetic.main.login_fragment.recoverPassText
import kotlinx.android.synthetic.main.login_fragment.signInButton
import kotlinx.android.synthetic.main.login_fragment.signUpText
import javax.inject.Inject

class LoginFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModels { vmFactory }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (loginFragmentEmailEditText.text?.isEmpty() == false
                && loginFragmentPasswordEditText.text?.isEmpty() == false
            ) {
                signInButton.apply {
                    isEnabled = true
                    alpha = 1f
                }
            } else {
                signInButton.apply {
                    isEnabled = false
                    alpha = 0.2f
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginFragmentEmailEditText.addTextChangedListener(textWatcher)
        loginFragmentPasswordEditText.addTextChangedListener(textWatcher)
        signUpText.setOnClickListener {
            // Navigate to Sign Up fragment
            val direction = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(direction)
        }
        recoverPassText.setOnClickListener {
            // Navigate to Recover pass fragment
            val direction = LoginFragmentDirections.actionLoginFragmentToRecoverPasswordFragment()
            findNavController().navigate(direction)
        }

        signInButton.apply {
            alpha = 0.2f
            isEnabled = false
            setOnClickListener {
                sendData()
            }
        }

        // LiveData Observer
        viewModel.loginDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<AuthResult, ErrorModel>> {
                handleUiStates(it) { auth ->
                    processNewValue(auth)
                }
            }
        )
    }

    override fun onError(error: ErrorModel) {
        enableLoginButton()
        loginFragmentProgressView.gone()
        when (error.errorCause) {
            LoginErrors.InvalidEmail -> {
                setErrorFields(loginFragmentEmailField, R.string.invalid_email)
                resetField(loginFragmentPasswordField)
            }
            LoginErrors.InvalidPassword -> {
                resetField(loginFragmentEmailField)
                setErrorFields(loginFragmentPasswordField, R.string.password_too_short)
            }
            FirebaseErrors.InvalidUser -> {
                Toast.makeText(
                    activity,
                    getString(R.string.firebase_invalid_user),
                    Toast.LENGTH_LONG
                ).show()
                firebaseAuth.signOut()
            }
            FirebaseErrors.InvalidCredentials -> {
                Toast.makeText(
                    activity,
                    getString(R.string.firebase_invalid_credentials),
                    Toast.LENGTH_LONG
                ).show()
                firebaseAuth.signOut()
            }
            else -> {
                Toast.makeText(activity, getString(R.string.error_message), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setErrorFields(view: TextInputLayout, messageIdRes: Int) {
        view.apply {
            helperText = getString(messageIdRes)
            setHintTextAppearance(R.style.ErrorTheme)
            setErrorTextAppearance(R.style.ErrorTheme)
            error = getString(messageIdRes)
            if (endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
                setEndIconTintMode(PorterDuff.Mode.SRC_IN)
                setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)))
            } else {
                setErrorIconDrawable(R.drawable.ic_error)
            }
        }
    }

    private fun resetField(view: TextInputLayout) {
        view.apply {
            helperText = ""
            setHintTextAppearance(R.style.App_Input_Hint)
            setErrorTextAppearance(0)
            error = ""
            setErrorIconDrawable(0)
            if (endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
                setEndIconTintMode(PorterDuff.Mode.SRC_IN)
                setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)))
            } else {
                setErrorIconDrawable(0)
            }
        }
    }

    override fun onLoading() {
        loginFragmentProgressView.visible()
    }

    private fun processNewValue(auth: AuthResult) {
        // TODO: Subscribe to user firebase chat topics
        loginFragmentProgressView.gone()
        val direction = LoginFragmentDirections.actionLoginFragmentToCategoryListFragment2()
        findNavController().navigate(direction)
    }

    private fun sendData() {
        resetField(loginFragmentEmailField)
        resetField(loginFragmentPasswordField)
        disableLoginButton()
        val email = loginFragmentEmailEditText.text.toString().replace(" ", "")
        val password = loginFragmentPasswordEditText.text.toString()
        viewModel.login(email, password)
    }

    private fun disableLoginButton() {
        signInButton.isEnabled = false
    }

    private fun enableLoginButton() {
        signInButton.isEnabled = true
    }
}
