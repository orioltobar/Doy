package com.napptilians.features.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Constants
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.Errors
import com.napptilians.commons.error.LoginErrors
import com.napptilians.domain.usecases.LoginUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<AuthResult>() {

    private val _loginDataStream = MutableLiveData<UiStatus<AuthResult, ErrorModel>>()
    val loginDataStream: LiveData<UiStatus<AuthResult, ErrorModel>> get() = _loginDataStream

    fun login(email: String, password: String) {
        val errors = checkCredentials(email, password)
        viewModelScope.launch {
            if (!errors.first) {
                _loginDataStream.value = emitFailure(ErrorModel(errorCause = errors.second))
            } else {
                _loginDataStream.value = emitLoadingState()
                val request = loginUseCase(email, password)
                _loginDataStream.value = processModel(request)
            }
        }

    }

    @SuppressLint("NewApi")
    private fun checkCredentials(
        email: String,
        password: String
    ): Pair<Boolean, Errors?> {
        // Expressed as when expression for improved readability.
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false to LoginErrors.InvalidEmail
            }
            password.length < Constants.MINIMUM_PASSWORD_LENGTH -> {
                false to LoginErrors.InvalidPassword
            }
            else -> true to null
        }
    }
}