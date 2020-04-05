package com.orioltobar.features

import com.orioltobar.commons.Failure
import com.orioltobar.commons.Response
import com.orioltobar.commons.Success

/**
 * Defines the possibles status of a view.
 */
sealed class UiStatus<out T, out V>

object Loading : UiStatus<Nothing, Nothing>()
class NewValue<T>(val result: T) : UiStatus<T, Nothing>()
class Error<V>(val error: V) : UiStatus<Nothing, V>()

fun <T, E> processResponse(action: Response<T, E>) =
    when (action) {
        is Success -> NewValue(action.result)
        is Failure -> Error(action.error)
    }