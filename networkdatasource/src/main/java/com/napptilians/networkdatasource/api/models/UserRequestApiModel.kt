package com.napptilians.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class UserRequestApiModel(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("uid")
    val uid: String? = null,

    @SerializedName("token")
    val pushToken: String? = null
)