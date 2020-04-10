package com.napptilians.doy.di.modules

import com.napptilians.doy.view.categorylist.CategoryListFragment
import com.napptilians.doy.view.chat.ChatFragment
import com.napptilians.doy.view.chat.ChatListFragment
import com.napptilians.doy.view.discover.DiscoverFragment
import com.napptilians.doy.view.intro.IntroFragment
import com.napptilians.doy.view.login.LoginFragment
import com.napptilians.doy.view.register.RegisterFragment
import com.napptilians.doy.view.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun bindDiscoverFragment(): DiscoverFragment

    @ContributesAndroidInjector
    internal abstract fun bindChatListFragment(): ChatListFragment

    @ContributesAndroidInjector
    internal abstract fun bindChatFragment(): ChatFragment

    @ContributesAndroidInjector
    internal abstract fun bindRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    internal abstract fun bindIntroFragment(): IntroFragment

    @ContributesAndroidInjector
    internal abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun bindCategoryListFragment(): CategoryListFragment

    @ContributesAndroidInjector
    internal abstract fun bindSplashFragment(): SplashFragment
}
