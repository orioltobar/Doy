package com.orioltobar.networkdatasource.utils

import com.orioltobar.commons.Failure
import com.orioltobar.commons.Response
import com.orioltobar.commons.Success
import com.orioltobar.commons.error.ErrorModel

/**
 * This function is used to wrap a [call] to an API in a safe way. The result is expressed as a
 * Coroutine Response which is a sealed class that contains the result of the operation.
 */
suspend fun <T> safeApiCall(
    call: suspend () -> T
): Response<T, ErrorModel> =
    try {
        val response = call()
        if (response == null) {
            Failure(ErrorModel(""))
        } else {
            Success(response)
        }
    } catch (exception: Exception) {
        Failure(ErrorModel(exception.message ?: ""))
    }