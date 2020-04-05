package com.orioltobar.androidklean.di.modules

import com.orioltobar.androidklean.view.discover.DiscoverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindDiscoverFragment(): DiscoverFragment
}