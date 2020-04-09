package com.napptilians.doy.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.MainActivity
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.register_fragment.*
import javax.inject.Inject

class RegisterFragment : BaseFragment() {

    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.register_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("FBauth: ${fireBaseAuth.app.name}")

        registerFragmentSignInText.setOnClickListener {
            val direction = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(direction)
        }

        registerFragmentCreateButton.setOnClickListener {
            sendRegister()
        }
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun sendRegister() {
        val user = registerFragmentEmailEditText.text.toString().replace(" ", "")
        // password 6 characters min
        val password = registerFragmentPasswordEditText.text.toString()

        val activity = activity as? MainActivity
        activity?.let {
            if (user.isNotEmpty() && password.isNotEmpty()) {
                fireBaseAuth.createUserWithEmailAndPassword(user, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            println("SUCCESS: ${task.result}")
                        } else {
                            println("FAILED")
                        }
                    }
            }
        }
    }
}