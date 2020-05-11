package com.napptilians.domain.repositories

import com.google.firebase.auth.AuthResult
import com.napptilians.commons.Response
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.domain.models.chat.ChatModel
import com.napptilians.domain.models.device.DeviceModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.models.user.UserModel
import kotlinx.coroutines.flow.Flow

interface DoyRepository {

    suspend fun getCategories(categoryIds: List<Long> = emptyList()): Response<List<CategoryModel>, ErrorModel>

    suspend fun getDeviceInfo(): Response<DeviceModel, ErrorModel>

    suspend fun saveDeviceInfo(device: DeviceModel): Response<Unit, ErrorModel>

    suspend fun addService(service: ServiceModel): Response<Long, ErrorModel>

    suspend fun addAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel>

    suspend fun deleteAttendee(userUid: String, serviceId: Long): Response<Unit, ErrorModel>

    suspend fun login(email: String, password: String): Response<AuthResult, ErrorModel>

    suspend fun register(
        name: String,
        password: String,
        email: String,
        token: String
    ): Response<Unit, ErrorModel>

    suspend fun logout(userUid: String)

    suspend fun recoverPassword(email: String): Response<Unit?, ErrorModel>

    suspend fun getServices(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null,
        ascending: Boolean = true
    ): Response<List<ServiceModel>, ErrorModel>

    suspend fun getUser(userUid: String): Response<UserModel, ErrorModel>

    suspend fun updateUser(
        userUid: String,
        name: String? = null,
        token: String? = null,
        description: String? = null,
        image: String? = null
    ): Response<UserModel, ErrorModel>

    suspend fun getMyServices(uid: String? = null): Response<List<ServiceModel>, ErrorModel>

    suspend fun sendChatMessage(chatId: String, message: ChatModel): Response<Unit, ErrorModel>

    fun getChatMessages(chatId: String): Flow<Response<ChatModel, ErrorModel>>
}
