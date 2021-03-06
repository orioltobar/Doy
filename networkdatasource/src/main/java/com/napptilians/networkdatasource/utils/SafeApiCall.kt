package com.napptilians.networkdatasource.utils

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.FirebaseErrors

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
    } catch (exception: FirebaseAuthInvalidUserException) {
        Failure(ErrorModel(null, FirebaseErrors.InvalidUser))
    } catch (exception: FirebaseAuthInvalidCredentialsException) {
        Failure(ErrorModel(null, FirebaseErrors.InvalidCredentials))
    } catch (exception: FirebaseAuthUserCollisionException) {
        Failure(ErrorModel(null, FirebaseErrors.UserAlreadyExists))
    } catch (exception: Exception) {
        Failure(ErrorModel(exception.message ?: ""))
    }
