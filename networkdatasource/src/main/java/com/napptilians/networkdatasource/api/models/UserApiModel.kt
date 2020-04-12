package com.napptilians.networkdatasource.api.models

import com.google.gson.annotations.SerializedName

data class UserApiModel(
    @SerializedName("user_id")
    val id: Long? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("uid")
    val uid: String? = null,

    @SerializedName("token")
    val pushToken: String? = null,

    @SerializedName("bio")
    val description: String? = null,

    @SerializedName("image")
    val image: String? = null
)