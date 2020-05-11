package com.napptilians.commons.error

sealed class FirebaseErrors : Errors {

    object InvalidUser : FirebaseErrors()
    object InvalidCredentials : FirebaseErrors()
    object UserAlreadyExists : FirebaseErrors()
    object ErrorSendingMessage : FirebaseErrors()
    object ErrorReceivingMessage : FirebaseErrors()
    object EmptyMessage : FirebaseErrors()
}
