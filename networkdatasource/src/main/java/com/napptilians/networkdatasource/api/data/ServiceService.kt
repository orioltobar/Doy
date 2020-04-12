package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.ServiceApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceService {

    @GET("/getServices")
    suspend fun getServices(
        @Query(CATEGORY_ID) categoryId: List<Long> = emptyList(),
        @Query(SERVICE_ID) serviceId: Long? = null,
        @Query(UID) uid: String? = null
    ): List<ServiceApiModel>

    @GET("/getMyServices")
    suspend fun getMyServices(
        @Query(UID) uid: Long
    ): List<ServiceApiModel>

    @POST("/addService")
    suspend fun addService(
        @Body service: ServiceApiModel
    ): Long

    @POST("/addAttendee")
    suspend fun addAttendee(
        @Query(UID) uid: String = "",
        @Query(SERVICE_ID) serviceId: Long? = null
    )

    companion object {
        private const val CATEGORY_ID = "category_id"
        private const val SERVICE_ID = "service_id"
        private const val UID = "uid"
    }
}
