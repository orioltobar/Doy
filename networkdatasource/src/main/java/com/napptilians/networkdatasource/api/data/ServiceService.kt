package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.ServiceApiModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceService {

    @POST("/addService")
    suspend fun addService(
        @Body service: ServiceApiModel
    ): Long

    companion object {
        private const val CATEGORY_ID = "category_id"
    }
}
