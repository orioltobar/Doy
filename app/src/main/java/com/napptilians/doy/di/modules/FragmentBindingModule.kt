package com.napptilians.doy.di.modules

import com.napptilians.doy.view.addservice.AddServiceFragment
import com.napptilians.doy.view.categorylist.CategoryListFragment
import com.napptilians.doy.view.chat.ChatFragment
import com.napptilians.doy.view.chat.ChatListFragment
import com.napptilians.doy.view.discover.DiscoverFragment
import com.napptilians.doy.view.intro.IntroFragment
import com.napptilians.doy.view.login.LoginFragment
import com.napptilians.doy.view.register.RegisterFragment
import com.napptilians.doy.view.servicelist.ServiceListFragment
import com.napptilians.doy.view.splash.SplashFragment
import com.napptilians.doy.view.selectduration.SelectDurationFragment
import com.napptilians.doy.view.selectspots.SelectSpotsFragment
import com.napptilians.doy.view.servicedetail.ServiceDetailFragment
import com.napptilians.doy.view.user.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
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
    internal abstract fun bindServiceListFragment(): ServiceListFragment

    @ContributesAndroidInjector
    internal abstract fun bindSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    internal abstract fun bindAddServiceFragment(): AddServiceFragment

    @ContributesAndroidInjector
    internal abstract fun bindSelectSpotsFragment(): SelectSpotsFragment

    @ContributesAndroidInjector
    internal abstract fun bindSelectDurationFragment(): SelectDurationFragment

    @ContributesAndroidInjector
    internal abstract fun bindProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    internal abstract fun bindServiceDetailFragment(): ServiceDetailFragment
}
