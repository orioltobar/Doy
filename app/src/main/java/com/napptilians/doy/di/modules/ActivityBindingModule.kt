package com.napptilians.doy.di.modules

import com.napptilians.doy.MainActivity
import com.napptilians.doy.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    @ActivityScope
    internal abstract fun bindMainActivity(): MainActivity
}