package com.napptilians.doy.view.recoverpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.LoginErrors
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.invisible
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.RecoverPasswordViewModel
import kotlinx.android.synthetic.main.recover_password_fragment.recoverPasswordProgressView
import kotlinx.android.synthetic.main.recover_password_fragment.recoverEmailEditText
import kotlinx.android.synthetic.main.recover_password_fragment.recoverEmailInput
import kotlinx.android.synthetic.main.recover_password_fragment.recoverPasswordButton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RecoverPasswordFragment : BaseFragment() {

    private val viewModel: RecoverPasswordViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.recover_password_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // LiveData Observer
        viewModel.recoverDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<Unit?, ErrorModel>> {
                handleUiStates(it) { auth ->
                    processResponse(
                        auth
                    )
                }
            }
        )

        recoverPasswordButton.apply {
            isEnabled = false
            alpha = 0.2f
            setOnClickListener {
                val textField = recoverEmailEditText.text.toString()
                viewModel.execute(textField)
            }
        }

        recoverEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isEmpty() == false) {
                    recoverPasswordButton.apply {
                        isEnabled = true
                        alpha = 1f
                    }
                } else {
                    recoverPasswordButton.apply {
                        isEnabled = false
                        alpha = 0.2f
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onLoading() {
        recoverPasswordProgressView.visible()
    }

    override fun onError(error: ErrorModel) {
        recoverPasswordProgressView.invisible()
        when (error.errorCause) {
            LoginErrors.InvalidEmail -> {
                setErrorFields(recoverEmailInput, R.string.invalid_email)
            }
            else -> {
                resetField(recoverEmailInput)
                activity?.let { DoyErrorDialog(it).show() }
            }
        }
    }

    private fun processResponse(response: Unit?) {
        resetField(recoverEmailInput)
        recoverPasswordProgressView.invisible()
        activity?.let { activity ->
            DoyDialog(activity).apply {
                setPopupIcon(R.drawable.ic_clap)
                setPopupTitle(context.resources.getString(R.string.recover_password_email_sent))
                setPopupSubtitle(context.resources.getString(R.string.recover_password_email_reminder))
                show()
                setOnDismissListener { findNavController().popBackStack() }
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
            setHintTextAppearance(R.style.App_Input_Hint)
            setErrorTextAppearance(0)
            error = ""
            setErrorIconDrawable(0)
        }
    }
}