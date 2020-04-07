package com.napptilians.features

import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success

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