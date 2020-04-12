package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.UserApiModel
import com.napptilians.networkdatasource.api.models.UserRequestApiModel
import com.napptilians.networkdatasource.api.models.UserUpdateRequestApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {

    @POST("/addUser")
    suspend fun addUser(
        @Body user: UserRequestApiModel
    )

    @PUT("/addUser")
    suspend fun updateUser(
        @Body user: UserUpdateRequestApiModel
    ): List<UserApiModel>

    @GET("/getUser")
    suspend fun getUser(
        @Query(USER_UID) userUid: String
    ): List<UserApiModel>

    companion object {
        private const val USER_UID = "uid"
    }
}