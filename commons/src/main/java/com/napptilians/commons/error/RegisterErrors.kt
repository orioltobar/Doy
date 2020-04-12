package com.napptilians.commons.error

sealed class RegisterErrors: Errors {

    object EmptyName : RegisterErrors()
    object InvalidEmail : RegisterErrors()
    object ShortPassword : RegisterErrors()
    object PasswordsDontMatch : RegisterErrors()
}
