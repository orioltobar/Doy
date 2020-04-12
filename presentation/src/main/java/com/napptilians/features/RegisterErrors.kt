package com.napptilians.features

import com.napptilians.commons.error.Errors

sealed class RegisterErrors: Errors {

    object ShortPassword : RegisterErrors()
    object PasswordsDontMatch : RegisterErrors()
    object InvalidEmail : RegisterErrors()
    object EmptyName : RegisterErrors()
}
