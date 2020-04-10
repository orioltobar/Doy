package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.ServiceApiModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceService {

    @GET("/getServices")
    suspend fun getServices(
        @Query(CATEGORY_ID) categoryId: Long
    ): List<ServiceApiModel>

    @POST("/addService")
    suspend fun addService(
        @Body service: ServiceApiModel
    ): Long

    companion object {
        private const val CATEGORY_ID = "category_id"
    }
}
