package com.napptilians.domain.models.user

data class UserModel(
    val name: String,
    val email: String,
    var uid: String = "",
    var pushToken: String = "",
    var description: String = "",
    var image: String = ""
)