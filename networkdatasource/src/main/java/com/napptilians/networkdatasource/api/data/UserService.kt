package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.UserRequestApiModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/addUser")
    suspend fun addUser(
        @Body user: UserRequestApiModel
    )
}