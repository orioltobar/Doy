package com.napptilians.doy.di.modules

import com.napptilians.data.datasources.DbDataSource
import com.napptilians.data.datasources.FirebaseDataSource
import com.napptilians.data.datasources.NetworkDataSource
import com.napptilians.data.repositories.DoyRepositoryImpl
import com.napptilians.diskdatasource.data.DbDataBaseImpl
import com.napptilians.domain.repositories.DoyRepository
import com.napptilians.networkdatasource.api.data.NetworkDataSourceImpl
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
    fun bindsMovieRepository(repository: DoyRepositoryImpl): DoyRepository

    @Binds
    @Singleton
    fun bindsNetworkDataSource(dataSource: NetworkDataSourceImpl): NetworkDataSource

    @Binds
    @Singleton
    fun provideMovieDataBaseDataSource(dataSource: DbDataBaseImpl): DbDataSource

    @Binds
    @Singleton
    fun provideFirebaseDataSource(firebaseDataSource: FirebaseDataSourceImpl): FirebaseDataSource

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
