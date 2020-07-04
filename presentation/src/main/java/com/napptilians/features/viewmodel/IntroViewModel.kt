package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.valueOrNull
import com.napptilians.domain.usecases.GetDeviceInfoUseCase
import com.napptilians.domain.usecases.LoginWithGoogleUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val getDeviceInfoUseCase: GetDeviceInfoUseCase
) : BaseViewModel<AuthResult>() {

    private val _loginWithGoogleDataStream = MutableLiveData<UiStatus<String, ErrorModel>>()
    val loginWithGoogleDataStream: LiveData<UiStatus<String, ErrorModel>> get() = _loginWithGoogleDataStream

    fun login(credential: AuthCredential) {
        viewModelScope.launch {
            _loginWithGoogleDataStream.value = emitLoadingState()
            val deviceTokenRequest = getDeviceInfoUseCase.execute()
            val deviceToken = deviceTokenRequest.valueOrNull()?.firebaseToken ?: ""
            val request = loginWithGoogleUseCase(credential, deviceToken)
            _loginWithGoogleDataStream.value = processModel(request)
        }
    }
}
