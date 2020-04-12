package com.napptilians.doy.view.recoverpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.LoginErrors
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.RecoverPasswordViewModel
import kotlinx.android.synthetic.main.recover_password_fragment.loginFragmentProgressView
import kotlinx.android.synthetic.main.recover_password_fragment.recoverEmailEditText
import kotlinx.android.synthetic.main.recover_password_fragment.recoverEmailInput
import kotlinx.android.synthetic.main.recover_password_fragment.recoverPasswordButton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RecoverPasswordFragment : BaseFragment(), ToolbarBehaviour {

    override val genericToolbar: Toolbar? by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

    private val viewModel: RecoverPasswordViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.recover_password_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableHomeAsUp(true) { findNavController().popBackStack() }

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

        recoverPasswordButton.setOnClickListener {
            val textField = recoverEmailEditText.text.toString()
            viewModel.execute(textField)
        }
    }

    override fun onLoading() {
        loginFragmentProgressView.visible()
    }

    override fun onError(error: ErrorModel) {
        loginFragmentProgressView.gone()
        when (error.errorCause) {
            LoginErrors.InvalidEmail -> {
                setErrorFields(recoverEmailInput, R.string.invalid_email)
            }
            else -> {
                activity?.let { DoyErrorDialog(it).show() }
            }
        }
    }

    private fun processResponse(response: Unit?) {
        loginFragmentProgressView.gone()
        activity?.let { activity ->
            DoyDialog(activity).apply {
                setPopupIcon(R.drawable.ic_clap)
                setPopupTitle(context.resources.getString(R.string.password_forgot))
                setPopupSubtitle(context.resources.getString(R.string.recover_password))
                show()
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
}