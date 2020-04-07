package com.napptilians.commons.error

data class ErrorModel(
    val errorMessage: String
): Throwable()