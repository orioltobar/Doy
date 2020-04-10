package com.napptilians.doy.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.register_fragment.*
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
        registerFragmentProgressView.visibility = View.GONE
        val direction = RegisterFragmentDirections.actionRegisterFragmentToMenuFavouritesListButton()
        findNavController().navigate(direction)
    }

    override fun onError(error: ErrorModel) {
        enableRegisterButton()
        registerFragmentProgressView.visibility = View.GONE
        val errorString = error.message
            ?.takeIf { it.isNotEmpty() }
            ?.let { it }
            ?: run { getString(R.string.generic_error) }

        Toast.makeText(activity, errorString, Toast.LENGTH_LONG).show()
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