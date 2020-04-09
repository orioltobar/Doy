package com.napptilians.networkdatasource.api.data

import com.napptilians.networkdatasource.api.models.CategoryApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryListService {

    @GET("/getCategories")
    suspend fun getCategories(
        @Query(CATEGORY_ID) categoryId: List<Long>
    ): List<CategoryApiModel>

    companion object {
        private const val CATEGORY_ID = "category_id"
    }
}
