package com.napptilians.commons.error

sealed class LoginErrors: Errors {

    object InvalidEmail : LoginErrors()
    object EmptyPassword : LoginErrors()
}
