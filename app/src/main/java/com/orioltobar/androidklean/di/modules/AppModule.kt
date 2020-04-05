package com.orioltobar.androidklean.di.modules

import com.orioltobar.commons.Constants.STRING_TO_BE_PROVIDED
import com.orioltobar.networkdatasource.di.BaseUrl
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ApplicationDataBindsModule::class])
object AppModule {

    @Provides
    @Named(STRING_TO_BE_PROVIDED)
    fun provideExampleString(): String = "exampleString"

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = "https://www.google.com"
}