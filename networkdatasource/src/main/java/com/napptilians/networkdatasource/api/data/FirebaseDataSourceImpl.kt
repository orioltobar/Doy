package com.napptilians.networkdatasource.api.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.map
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.networkdatasource.utils.safeApiCall
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDataSourceImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    FirebaseDataSource {

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        safeApiCall { firebaseAuth.signInWithEmailAndPassword(email, password).await() }
            .map { it }

    override suspend fun register(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        safeApiCall {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }.map { it }

    override suspend fun removeCurrentUser(): Response<Unit?, ErrorModel> =
        safeApiCall {
            firebaseAuth.currentUser?.delete()?.await()
        }.map { Unit }
}