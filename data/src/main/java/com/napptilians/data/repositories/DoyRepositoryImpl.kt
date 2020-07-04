package com.napptilians.data.repositories

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Failure
import com.napptilians.commons.Response
import com.napptilians.commons.Success
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.commons.flatMap
import com.napptilians.commons.map
import com.napptilians.commons.singleSourceOfTruth
import com.napptilians.commons.valueOrNull
import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.models.user.UserModel
import com.napptilians.domain.repositories.DoyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DoyRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dbDataSource: DbDataSource,
    private val firebaseDataSource: FirebaseDataSource
) : DoyRepository {

    override suspend fun getCategories(categoryIds: List<Long>): Response<List<CategoryModel>, ErrorModel> {
        return networkDataSource.getCategories(categoryIds)
    }

    override suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel> =
        dbDataSource.getDeviceInfo()

    override suspend fun saveDeviceInfo(device: DeviceModel): Response<Unit, ErrorModel> =
        dbDataSource.saveDeviceInfo(device)

    override suspend fun addService(service: ServiceModel): Response<Long, ErrorModel> =
        networkDataSource.addService(service)

    override suspend fun addAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel> =
        networkDataSource.addAttendee(userUid, serviceId)

    override suspend fun deleteAttendee(
        userUid: String,
        serviceId: Long
    ): Response<Unit, ErrorModel> =
        networkDataSource.deleteAttendee(userUid, serviceId)

    override suspend fun login(
        email: String,
        password: String
    ): Response<AuthResult, ErrorModel> =
        firebaseDataSource.login(email, password)

    override suspend fun loginWithGoogle(
        credential: AuthCredential,
        idToken: String
    ): Response<String, ErrorModel> {
        // Login in with Google through Firebase
        val fireBaseRequest = firebaseDataSource.loginWithGoogle(credential)

        return fireBaseRequest.either(
            onSuccess = { authResult ->
                // Then send tokens to serve
                val displayName = authResult.user?.displayName ?: ""
                val email = authResult.user?.email ?: ""
                val uid = authResult.user?.uid ?: ""
                val request = networkDataSource.addUser(displayName, email, uid, idToken)
                request.either(
                    onSuccess = { Success(authResult.user?.displayName ?: "") },
                    onFailure = { error ->
                        firebaseDataSource.removeCurrentUser()
                        Failure(error)
                    }
                )
            },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun register(
        name: String, password: String, email: String, token: String
    ): Response<Unit, ErrorModel> {

        // Register in Firebase
        val fireBaseRequest = firebaseDataSource.register(email, password)

        return fireBaseRequest.either(
            onSuccess = {
                // Then send tokens to serve
                val uid = it.user?.uid ?: ""
                val request = networkDataSource.addUser(name, email, uid, token)
                request.either(
                    onSuccess = { Success(Unit) },
                    onFailure = { error ->
                        firebaseDataSource.removeCurrentUser()
                        Failure(error)
                    }
                )
            },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun logout(userUid: String) {
        dbDataSource.removeUser(userUid)
        networkDataSource.updateUser(userUid = userUid, token = "")
    }

    override suspend fun recoverPassword(email: String): Response<Unit?, ErrorModel> =
        firebaseDataSource.recoverPassword(email)

    override suspend fun getServices(
        categoryIds: List<Long>,
        serviceId: Long?,
        uid: String?,
        ascending: Boolean
    ): Response<List<ServiceModel>, ErrorModel> {
        return networkDataSource.getServices(categoryIds, serviceId, uid).map {
            it.forEach { service ->
                // Service is full when there are no available spots
                // and the user is not one of the attendees or its owner
                service.isFull = service.attendees ?: 0 == service.spots ?: 0
                        && !service.assistance
                        && service.ownerId?.equals(uid) == false
            }
            if (ascending) {
                it.sortedWith(comparatorAscending)
            } else {
                it.sortedWith(comparatorDescending)
            }
        }
    }

    override suspend fun getUser(userUid: String): Response<UserModel, ErrorModel> =
        singleSourceOfTruth(
            dbDataSource = { dbDataSource.getUser(userUid) },
            networkDataSource = { networkDataSource.getUser(userUid) },
            dbCallback = { user ->
                dbDataSource.saveUser(user)
                dbDataSource.getUser(user.uid)
            }
        )

    override suspend fun updateUser(
        userUid: String,
        name: String?,
        token: String?,
        description: String?,
        image: String?
    ): Response<UserModel, ErrorModel> {
        val request = networkDataSource.updateUser(userUid, name, token, description, image)
        return request.flatMap { user ->
            dbDataSource.saveUser(user)
            dbDataSource.getUser(user.uid)
        }
    }

    override suspend fun getMyServices(uid: String?): Response<List<ServiceModel>, ErrorModel> {
        return networkDataSource.getMyServices(uid).map {
            it.sortedWith(comparatorAscending)
        }
    }

    override suspend fun sendChatMessage(
        chatId: String,
        message: ChatModel
    ): Response<Unit, ErrorModel> = firebaseDataSource.sendChatMessage(chatId, message)

    override fun getChatMessages(chatId: String): Flow<Response<ChatModel, ErrorModel>> =
        flow {
            firebaseDataSource.getChatMessages(chatId).collect { response ->
                response.valueOrNull()?.let { networkChat ->
                    dbDataSource.insertChatMessage(networkChat)
                    val dbChat =
                        dbDataSource.getChatMessage(networkChat.chatId, networkChat.timeStamp)
                    emit(dbChat)
                }
            }
        }

    override fun getLastChatMessage(chatId: String): Flow<Response<Pair<ChatModel, Int>, ErrorModel>> =
        flow {
            firebaseDataSource.getLastChatMessage(chatId).collect { response ->
                response.valueOrNull()?.let { networkChatList ->
                    networkChatList.forEach { networkChat ->
                        dbDataSource.insertChatMessage(
                            networkChat
                        )
                    }
                }
                dbDataSource.getChatMessages(chatId).valueOrNull()?.let { dbList ->
                    val unreadMessages = dbList.filter { !it.read }.size
                    dbList.takeIf { it.isNotEmpty() }?.let { safeList ->
                        val chatWithUnreadMessages = Pair(safeList.last(), unreadMessages)
                        emit(Success(chatWithUnreadMessages))
                    }
                }
            }
        }

    override suspend fun updateMessageReadStatus(message: ChatModel) =
        dbDataSource.updateMessageReadStatus(message)

    override suspend fun deleteService(serviceId: Long): Response<Unit, ErrorModel> =
        networkDataSource.deleteService(serviceId)

    private val comparatorAscending: Comparator<ServiceModel>
        get() = compareBy({ it.isFull }, { it.date }, { it.name })

    private val comparatorDescending: Comparator<ServiceModel>
        get() = compareByDescending { it.date }

}
