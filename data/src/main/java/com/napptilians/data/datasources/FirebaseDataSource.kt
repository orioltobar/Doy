package com.napptilians.data.datasources

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel

interface FirebaseDataSource {

    suspend fun login(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun register(email:String, password:String): Response<AuthResult, ErrorModel>

    suspend fun removeCurrentUser(): Response<Unit?, ErrorModel>
}