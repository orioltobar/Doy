package com.napptilians.features.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.Errors
import com.napptilians.commons.error.LoginErrors
import com.napptilians.domain.usecases.RecoverPasswordUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.processResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecoverPasswordViewModel @Inject constructor(
    private val recoverPasswordUseCase: RecoverPasswordUseCase
) : BaseViewModel<RecoverPasswordViewModel>() {

    private val _recoverDataStream = MutableLiveData<UiStatus<Unit?, ErrorModel>>()
    val recoverDataStream: LiveData<UiStatus<Unit?, ErrorModel>> get() = _recoverDataStream

    fun execute(email: String) {
        val errors = checkEmail(email)
        if (!errors.first) {
            _recoverDataStream.value = emitFailure(ErrorModel(errorCause = errors.second))
        } else {
            viewModelScope.launch {
                _recoverDataStream.value = emitLoadingState()
                val request = recoverPasswordUseCase.invoke(email)
                _recoverDataStream.value = processResponse(request)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkEmail(
        email: String
    ): Pair<Boolean, Errors?> {
        // Expressed as when expression for improved readability.
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false to LoginErrors.InvalidEmail
            }
            else -> true to null
        }
    }
}
