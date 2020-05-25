package com.napptilians.networkdatasource.api.data

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.error.FirebaseErrors
import com.napptilians.commons.map
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.networkdatasource.utils.safeApiCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FirebaseDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FirebaseDataSource {

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        safeApiCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }.map { it }

    override suspend fun loginWithGoogle(credential: AuthCredential): Response<AuthResult, ErrorModel> =
        safeApiCall {
            firebaseAuth.signInWithCredential(credential).await()
        }.map { it }

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

    override suspend fun recoverPassword(email: String): Response<Unit?, ErrorModel> =
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Success(Unit)
        } catch (e: Exception) {
            Failure(ErrorModel(null, FirebaseErrors.InvalidCredentials))
        }

    override suspend fun sendChatMessage(
        chatId: String,
        message: ChatModel
    ): Response<Unit, ErrorModel> =
        try {
            firestore
                .collection(FIRESTORE_CHAT_TABLE)
                .document(chatId)
                .collection(FIRESTORE_CHAT_MESSAGE)
                .add(message)
                .await()
            Success(Unit)
        } catch (e: Exception) {
            Failure(ErrorModel(null, FirebaseErrors.ErrorSendingMessage))
        }

    override fun getChatMessages(chatId: String): Flow<Response<ChatModel, ErrorModel>> =
        callbackFlow {
            val subscription = firestore
                .collection(FIRESTORE_CHAT_TABLE)
                .document(chatId)
                .collection(FIRESTORE_CHAT_MESSAGE)
                .orderBy("timeStamp", Query.Direction.ASCENDING)
                .addSnapshotListener { messageSnapshot, firebaseError ->
                    firebaseError?.let {
                        val error =
                            Failure(ErrorModel(it.message, FirebaseErrors.ErrorReceivingMessage))
                        offer(error)
                        return@addSnapshotListener

                    }
                    if (messageSnapshot == null || messageSnapshot.isEmpty) {
                        val error = Failure(ErrorModel(null, FirebaseErrors.EmptyMessage))
                        offer(error)
                        return@addSnapshotListener
                    }

                    for (data in messageSnapshot.documents) {
                        val message = ChatModel(
                            data["chatId"] as String,
                            data["message"] as String,
                            data["senderName"] as String,
                            data["senderId"] as String,
                            data["timeStamp"] as Long
                        )
                        offer(Success(message))
                    }
                }

            awaitClose { subscription.remove() }
        }

    companion object {
        private const val FIRESTORE_CHAT_TABLE = "Chats"
        private const val FIRESTORE_CHAT_MESSAGE = "messages"
    }
}