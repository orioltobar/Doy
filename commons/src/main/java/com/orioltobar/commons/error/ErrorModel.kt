package com.orioltobar.commons.error

data class ErrorModel(
    val errorMessage: String
): Throwable()