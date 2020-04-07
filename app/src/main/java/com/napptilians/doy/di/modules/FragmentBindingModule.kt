package com.napptilians.doy.di.modules

import com.napptilians.doy.view.discover.DiscoverFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindDiscoverFragment(): DiscoverFragment
}