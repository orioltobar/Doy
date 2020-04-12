package com.napptilians.networkdatasource.api.mappers

import com.napptilians.commons.Mapper
import com.napptilians.domain.models.user.UserModel
import com.napptilians.networkdatasource.api.models.UserApiModel
import javax.inject.Inject

class UserMapper @Inject constructor() : Mapper<UserApiModel, UserModel> {

    override fun map(from: UserApiModel?): UserModel =
        UserModel(
            from?.id ?: -1L,
            from?.name ?: "",
            from?.email ?: "",
            from?.uid ?: "",
            from?.pushToken ?: "",
            from?.description ?: "",
            from?.image ?: ""
        )
}