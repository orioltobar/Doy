package com.napptilians.features.base

import androidx.lifecycle.ViewModel
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.features.Error
import com.napptilians.features.Loading
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus

// TODO: Finish.
abstract class BaseViewModel<T> : ViewModel() {

    fun <T, E> processModel(action: Response<T, E>): UiStatus<T, E> =
        when (action) {
            is Success -> NewValue(action.result)
            is Failure -> Error(action.error)
        }

    fun emitLoadingState(): Loading = Loading
}
