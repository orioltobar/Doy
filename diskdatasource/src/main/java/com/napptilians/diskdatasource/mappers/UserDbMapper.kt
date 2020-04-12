package com.napptilians.diskdatasource.mappers

import com.napptilians.commons.Mapper
import com.napptilians.diskdatasource.models.UserDbModel
import com.napptilians.domain.models.user.UserModel
import javax.inject.Inject

class UserDbMapper @Inject constructor() : Mapper<UserDbModel, UserModel> {

    override fun map(from: UserDbModel?): UserModel =
        UserModel(
            from?.id ?: -1L,
            from?.name ?: "",
            from?.email ?: "",
            from?.uid ?: "",
            from?.token ?: "",
            from?.bio ?: "",
            from?.image ?: ""
        )

    fun mapToDbModel(from: UserModel): UserDbModel =
        UserDbModel(
            from.uid,
            from.id,
            from.name,
            from.email,
            from.pushToken,
            from.description,
            from.image
        )
}