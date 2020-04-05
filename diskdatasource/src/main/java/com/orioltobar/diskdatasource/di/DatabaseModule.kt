package com.orioltobar.diskdatasource.di

import android.content.Context
import com.orioltobar.diskdatasource.dao.ExampleDao
import com.orioltobar.diskdatasource.database.AppDataBase
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
}