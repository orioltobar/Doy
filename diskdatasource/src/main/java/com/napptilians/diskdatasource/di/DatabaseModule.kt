package com.napptilians.diskdatasource.di

import android.content.Context
import com.napptilians.diskdatasource.dao.DeviceDao
import com.napptilians.diskdatasource.dao.ExampleDao
import com.napptilians.diskdatasource.dao.UserDao
import com.napptilians.diskdatasource.database.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): AppDataBase = AppDataBase.getInstance(context)

    @Provides
    @Singleton
    fun provideMovieDao(appDataBase: AppDataBase): ExampleDao = appDataBase.movieDao()

    @Provides
    @Singleton
    fun provideDeviceDao(appDataBase: AppDataBase): DeviceDao = appDataBase.deviceDao()

    @Provides
    @Singleton
    fun provideUserDao(appDataBase: AppDataBase): UserDao = appDataBase.userDao()
}