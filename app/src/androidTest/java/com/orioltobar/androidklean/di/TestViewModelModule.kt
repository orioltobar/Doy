package com.orioltobar.androidklean.di

import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
object TestViewModelModule {

    val viewModelFactory: ViewModelFactory = mockk()

    @Provides
    @JvmStatic
    fun providesViewModelFactory(): ViewModelFactory =
        viewModelFactory
}