package com.orioltobar.androidklean.dagger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orioltobar.androidklean.App
import com.orioltobar.androidklean.di.modules.ActivityBindingModule
import com.orioltobar.androidklean.di.modules.AppModule
import com.orioltobar.commons.Constants
import com.orioltobar.networkdatasource.di.BaseUrl
import com.orioltobar.networkdatasource.interceptors.UrlParamInterceptor
import com.orioltobar.networkdatasource.providers.NetworkProvider
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        TestNetworkModule::class]
)

interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context, @BindsInstance @BaseUrl baseUrl: String): AppComponent
    }
}

@Module
object TestNetworkModule {

    @Provides
    @BaseUrl
    fun provideBaseUrl() = "http://127.0.0.1:8080"

    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttpBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit =
        retrofitBuilder.client(okHttpBuilder.build()).build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        @BaseUrl url: String,
        gsonConverterFactory:
        GsonConverterFactory
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(gsonConverterFactory)

    @Provides
    @Singleton
    fun provideHttpBuilder(
        networkProvider: NetworkProvider
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().apply {
            addInterceptor(UrlParamInterceptor(networkProvider))
            addInterceptor(HttpLoggingInterceptor().apply {
                // TODO: Add isDebug variable to switch between Level.BODY and Level.NONE
                level = HttpLoggingInterceptor.Level.BODY
            })
            readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
        }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideNetworkProvider(
        @Named(Constants.STRING_TO_BE_PROVIDED) apiKey: String
    ): NetworkProvider = object : NetworkProvider {
        override val valueToBeProvided: String
            get() = apiKey
    }

    const val RETROFIT_TIMEOUT = 60L
}