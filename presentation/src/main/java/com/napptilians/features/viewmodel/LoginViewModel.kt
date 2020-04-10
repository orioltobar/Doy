package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.napptilians.commons.error.ErrorModel
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
        viewModelScope.launch {
            _loginDataStream.value = emitLoadingState()
            val request = loginUseCase(email, password)
            _loginDataStream.value = processModel(request)
        }
    }
}