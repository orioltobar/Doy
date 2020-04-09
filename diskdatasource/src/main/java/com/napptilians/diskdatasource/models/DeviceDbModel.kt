package com.napptilians.diskdatasource.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_info")
data class DeviceDbModel(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "firebase_token")
    val firebaseToken: String
)
