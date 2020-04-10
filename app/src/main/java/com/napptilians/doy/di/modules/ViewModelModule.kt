package com.napptilians.doy.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.napptilians.doy.di.ViewModelFactory
import com.napptilians.doy.di.ViewModelKey
import com.napptilians.features.viewmodel.CategoriesViewModel
import com.napptilians.features.viewmodel.LoginViewModel
import com.napptilians.features.viewmodel.MovieViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    internal abstract fun movieViewModel(viewModel: MovieViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    internal abstract fun categoriesViewModel(viewModel: CategoriesViewModel): ViewModel
}
