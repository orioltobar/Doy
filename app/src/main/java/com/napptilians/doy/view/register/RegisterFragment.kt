package com.napptilians.doy.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.features.RegisterErrors
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.register_fragment.registerFragmentCreateButton
import kotlinx.android.synthetic.main.register_fragment.registerFragmentEmailEditText
import kotlinx.android.synthetic.main.register_fragment.registerFragmentEmailField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentNameEditText
import kotlinx.android.synthetic.main.register_fragment.registerFragmentNameField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentPasswordEditText
import kotlinx.android.synthetic.main.register_fragment.registerFragmentPasswordField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentProgressView
import kotlinx.android.synthetic.main.register_fragment.registerFragmentRepeatPasswordEditText
import kotlinx.android.synthetic.main.register_fragment.registerFragmentRepeatPasswordField
import kotlinx.android.synthetic.main.register_fragment.registerFragmentSignInText
import javax.inject.Inject

class RegisterFragment : BaseFragment() {

    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    private val viewModel: RegisterViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.register_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerFragmentSignInText.setOnClickListener {
            val direction = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(direction)
        }
        registerFragmentCreateButton.setOnClickListener {
            sendRegister()
        }

        // LiveData Observer
        viewModel.registerDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Unit, ErrorModel>> {
                handleUiStates(it) { moveToMainScreen() }
            }
        )
    }

    private fun moveToMainScreen() {
        activity?.let { activity ->
            DoyDialog(activity).apply {
                setPopupIcon(R.drawable.ic_clap)
                setPopupTitle(context.resources.getString(R.string.account_created))
                setPopupSubtitle(context.resources.getString(R.string.welcome))
                setPopupFooterMessage(context.resources.getString(R.string.start_enjoying))
                setPopupFooterImage(R.drawable.popup_footer)
                show()
                setOnDismissListener {
                    val direction =
                        RegisterFragmentDirections.actionRegisterFragmentToMenuFavouritesListButton()
                    findNavController().navigate(direction)
                }
            }
        }
        registerFragmentProgressView.gone()
    }

    override fun onError(error: ErrorModel) {
        enableRegisterButton()
        registerFragmentProgressView.visibility = View.GONE
        when (error.errorCause) {
            RegisterErrors.EmptyName -> {
                setErrorFields(registerFragmentNameField, R.string.empty_name)
                resetField(registerFragmentEmailField)
                resetField(registerFragmentPasswordField)
                resetField(registerFragmentRepeatPasswordField)
            }
            RegisterErrors.InvalidEmail -> {
                resetField(registerFragmentNameField)
                setErrorFields(registerFragmentEmailField, R.string.invalid_email)
                resetField(registerFragmentPasswordField)
                resetField(registerFragmentRepeatPasswordField)
            }
            RegisterErrors.ShortPassword -> {
                resetField(registerFragmentNameField)
                resetField(registerFragmentEmailField)
                setErrorFields(registerFragmentPasswordField, R.string.password_too_short)
                setErrorFields(registerFragmentRepeatPasswordField, R.string.password_too_short)
            }
            RegisterErrors.PasswordsDontMatch -> {
                resetField(registerFragmentNameField)
                resetField(registerFragmentEmailField)
                setErrorFields(registerFragmentPasswordField, R.string.passwords_dont_match)
                setErrorFields(registerFragmentRepeatPasswordField, R.string.passwords_dont_match)
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
        registerFragmentProgressView.visibility = View.VISIBLE
    }

    private fun sendRegister() {
        disableRegisterButton()
        val name = registerFragmentNameEditText.text.toString()
        val email = registerFragmentEmailEditText.text.toString().replace(" ", "")
        val password = registerFragmentPasswordEditText.text.toString()
        val repeatPassword = registerFragmentRepeatPasswordEditText.text.toString()
        viewModel.register(name, email, password, repeatPassword)
    }

    private fun disableRegisterButton() {
        registerFragmentCreateButton.isClickable = false
    }

    private fun enableRegisterButton() {
        registerFragmentCreateButton.isClickable = true
    }
}
