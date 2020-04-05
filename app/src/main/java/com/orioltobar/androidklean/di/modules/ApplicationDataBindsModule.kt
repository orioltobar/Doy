package com.orioltobar.androidklean.di.modules

import com.orioltobar.data.datasources.DbDataSource
import com.orioltobar.data.datasources.NetworkDataSource
import com.orioltobar.data.repositories.ExampleRepositoryImpl
import com.orioltobar.diskdatasource.data.ExampleDataBaseImpl
import com.orioltobar.domain.repositories.ExampleRepository
import com.orioltobar.networkdatasource.api.data.ExampleDataSourceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Module that binds dependencies for repositories and datasources
 */
@Module
interface ApplicationDataBindsModule {

    @Binds
    @Singleton
    fun bindsMovieRepository(repository: ExampleRepositoryImpl): ExampleRepository

    @Binds
    @Singleton
    fun bindsMovieDataSource(dataSource: ExampleDataSourceImpl): NetworkDataSource

    @Binds
    @Singleton
    fun provideMovieDataBaseDataSource(dataSource: ExampleDataBaseImpl): DbDataSource

    @Module
    companion object {

        // TODO: Inject Adapters here.
//        @Provides
//        @ActivityScope
//        fun provideMovieListAdapter(): MovieListAdapter = MovieListAdapter()
//
//        @Provides
//        @ActivityScope
//        fun provideGenreListAdapter(): GenreListAdapter = GenreListAdapter()
    }
}