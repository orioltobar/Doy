package com.napptilians.doy.di.components

import android.content.Context
import com.napptilians.doy.App
import com.napptilians.diskdatasource.di.DatabaseModule
import com.napptilians.doy.di.modules.*
import com.napptilians.networkdatasource.di.NetworkModule
import com.napptilians.networkdatasource.di.NetworkServicesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        ServiceBindingModule::class,
        NetworkModule::class,
        NetworkServicesModule::class,
        ViewModelModule::class,
        DatabaseModule::class]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }
}