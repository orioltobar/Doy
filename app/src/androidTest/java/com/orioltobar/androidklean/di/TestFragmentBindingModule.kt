package com.orioltobar.androidklean.di

import com.orioltobar.androidklean.view.discover.DiscoverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class TestFragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindDiscoverFragment(): DiscoverFragment
}
