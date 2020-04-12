package com.napptilians.commons.error

sealed class FirebaseErrors: Errors {

    object InvalidUser : FirebaseErrors()
    object InvalidCredentials : FirebaseErrors()
}
