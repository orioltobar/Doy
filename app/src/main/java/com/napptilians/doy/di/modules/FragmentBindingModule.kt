package com.napptilians.doy.di.modules

import com.napptilians.doy.view.categorylist.CategoryListFragment
import com.napptilians.doy.view.chat.ChatFragment
import com.napptilians.doy.view.chat.ChatListFragment
import com.napptilians.doy.view.discover.DiscoverFragment
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
    internal abstract fun bindCategoryListFragment(): CategoryListFragment
}
