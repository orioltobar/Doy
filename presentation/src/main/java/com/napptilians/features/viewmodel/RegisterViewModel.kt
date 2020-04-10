package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.Constants
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.valueOrNull
import com.napptilians.domain.models.user.UserModel
import com.napptilians.domain.usecases.GetDeviceInfoUseCase
import com.napptilians.domain.usecases.RegisterUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase
) : BaseViewModel<UserModel>() {

    private val _registerDataStream = MutableLiveData<UiStatus<Unit, ErrorModel>>()
    val registerDataStream: LiveData<UiStatus<Unit, ErrorModel>> get() = _registerDataStream

    init {
        viewModelScope.launch {
            val deviceTokenRequest = getDeviceInfoUseCase.execute()
            val deviceToken = deviceTokenRequest.valueOrNull()?.firebaseToken ?: ""
            println("This is de devicetoken: $deviceToken")
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        repeatPassword: String
    ) {
        viewModelScope.launch {
            if (!checkCredentials(name, email, password, repeatPassword)) {
                _registerDataStream.value = emitFailure(ErrorModel(""))
            } else {
                val deviceTokenRequest = getDeviceInfoUseCase.execute()
                val deviceToken = deviceTokenRequest.valueOrNull()?.firebaseToken ?: ""
                _registerDataStream.value = emitLoadingState()
                val request = registerUseCase(name, email, password, deviceToken)
                _registerDataStream.value = processModel(request)
            }
        }
    }

    private fun checkCredentials(
        name: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        // Expressed as when expression for improved readability.
        return when {
            password.length < Constants.MINIMUM_PASSWORD_LENGTH -> {
                false
            }
            password != repeatPassword -> {
                false
            }
            email.isEmpty() -> {
                false
            }
            else -> name.isNotEmpty()
        }
    }
}