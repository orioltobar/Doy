package com.napptilians.diskdatasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.napptilians.diskdatasource.models.UserDbModel

@Dao
interface UserDao  {

    @Query("SELECT * FROM user WHERE uid == :uid")
    suspend fun getUser(uid: String): UserDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDbModel)

    @Query("DELETE FROM user WHERE uid = :uid")
    suspend fun deleteUser(uid: String)
}