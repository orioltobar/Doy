package com.napptilians.commons.error

data class ErrorModel(
    val errorMessage: String? = null,
    val errorCause: Errors? = null
) : Throwable()
