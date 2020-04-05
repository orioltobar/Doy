package com.orioltobar.androidklean.di

import com.orioltobar.androidklean.App
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class TestApplication : App() {

    private lateinit var testAppComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        testAppComponent = DaggerTestAppComponent.builder().application(this).build()
        testAppComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>
}
