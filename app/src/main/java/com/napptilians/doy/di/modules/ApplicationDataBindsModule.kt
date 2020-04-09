package com.napptilians.doy.di.modules

import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.data.repositories.ExampleRepositoryImpl
import com.napptilians.diskdatasource.data.DbDataBaseImpl
import com.napptilians.domain.repositories.ExampleRepository
import com.napptilians.networkdatasource.api.data.ExampleDataSourceImpl
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
    fun provideMovieDataBaseDataSource(dataSource: DbDataBaseImpl): DbDataSource

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