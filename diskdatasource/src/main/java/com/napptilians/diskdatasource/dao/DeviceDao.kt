package com.napptilians.diskdatasource.dao

import androidx.room.*
import com.napptilians.diskdatasource.models.DeviceDbModel

@Dao
interface DeviceDao {

    @Query("SELECT * FROM device_info")
    suspend fun getDeviceInfo(): DeviceDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceInfo(device: DeviceDbModel)
}
