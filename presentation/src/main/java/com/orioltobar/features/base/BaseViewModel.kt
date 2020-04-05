package com.orioltobar.features.base

import androidx.lifecycle.ViewModel
import com.orioltobar.commons.Failure
import com.orioltobar.commons.Response
import com.orioltobar.commons.Success
import com.orioltobar.features.Error
import com.orioltobar.features.Loading
import com.orioltobar.features.NewValue
import com.orioltobar.features.UiStatus

//TODO: Finish.
abstract class BaseViewModel<T> : ViewModel() {

    fun <T, E> processModel(action: Response<T, E>): UiStatus<T, E> =
        when(action) {
            is Success -> NewValue(action.result)
            is Failure -> Error(action.error)
        }

    fun emitLoadingState(): Loading = Loading
}
