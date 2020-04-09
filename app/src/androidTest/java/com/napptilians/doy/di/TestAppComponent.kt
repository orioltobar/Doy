package com.napptilians.doy.di

import android.app.Application
import com.napptilians.doy.di.components.AppComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi

@UseExperimental(ExperimentalCoroutinesApi::class)
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestViewModelModule::class,
        TestFragmentBindingModule::class,
        TestViewModelModule::class
    ]
)
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): TestAppComponent.Builder

        fun build(): TestAppComponent
    }

    fun inject(application: TestApplication)
}
