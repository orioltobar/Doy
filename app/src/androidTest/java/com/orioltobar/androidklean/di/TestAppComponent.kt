package com.orioltobar.androidklean.di

import android.app.Application
import com.orioltobar.androidklean.di.components.AppComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

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
