package com.napptilians.doy.di.modules

import com.napptilians.commons.AppDispatchers
import com.napptilians.commons.Constants.STRING_TO_BE_PROVIDED
import com.napptilians.networkdatasource.di.BaseUrl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [ApplicationDataBindsModule::class])
object AppModule {

    @Provides
    @Named(STRING_TO_BE_PROVIDED)
    fun provideExampleString(): String = "exampleString"

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "https://20200409t155054-dot-doy-proj.appspot.com/"

    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers = object : AppDispatchers {
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.IO
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    }
}