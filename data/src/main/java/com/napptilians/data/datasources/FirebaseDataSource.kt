package com.napptilians.data.datasources

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {

    suspend fun login(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun loginWithGoogle(credential: AuthCredential): Response<AuthResult, ErrorModel>

    suspend fun register(email:String, password:String): Response<AuthResult, ErrorModel>

    suspend fun removeCurrentUser(): Response<Unit?, ErrorModel>

    suspend fun recoverPassword(email: String): Response<Unit?, ErrorModel>

    suspend fun sendChatMessage(chatId: String, message: ChatModel): Response<Unit, ErrorModel>

    fun getChatMessages(chatId: String): Flow<Response<ChatModel, ErrorModel>>
}