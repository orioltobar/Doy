package com.orioltobar.androidklean.di.modules

import com.orioltobar.androidklean.MainActivity
import com.orioltobar.androidklean.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    @ActivityScope
    internal abstract fun bindMainActivity(): MainActivity
}