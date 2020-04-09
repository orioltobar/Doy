package com.napptilians.doy.di.modules

import com.napptilians.doy.di.scopes.ApplicationScope
import com.napptilians.doy.push.PushNotificationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBindingModule {

    @ContributesAndroidInjector
    @ApplicationScope
    internal abstract fun bindPushNotificationService(): PushNotificationService
}
