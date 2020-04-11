package com.napptilians.networkdatasource.api.mappers

import com.napptilians.commons.Mapper
import com.napptilians.domain.models.category.CategoryModel
import com.napptilians.networkdatasource.api.models.CategoryApiModel
import javax.inject.Inject

class CategoryMapper @Inject constructor() : Mapper<CategoryApiModel, CategoryModel> {

    override fun map(from: CategoryApiModel?): CategoryModel =
        CategoryModel(
            from?.categoryId ?: -1L,
            from?.pictureUrl ?: "",
            from?.name ?: ""
        )
}
