package com.napptilians.diskdatasource.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserDbModel(
    @PrimaryKey
    val uid: String,

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "token")
    val token: String,

    @ColumnInfo(name = "bio")
    val bio: String,

    @ColumnInfo(name = "image")
    val image: String
)