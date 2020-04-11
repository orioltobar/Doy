package com.napptilians.doy.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.napptilians.doy.di.ViewModelFactory
import com.napptilians.doy.di.ViewModelKey
import com.napptilians.features.viewmodel.AddServiceViewModel
import com.napptilians.features.viewmodel.CategoriesViewModel
import com.napptilians.features.viewmodel.LoginViewModel
import com.napptilians.features.viewmodel.MovieViewModel
import com.napptilians.features.viewmodel.ProfileViewModel
import com.napptilians.features.viewmodel.ServicesViewModel
import com.napptilians.features.viewmodel.RegisterViewModel
import com.napptilians.features.viewmodel.SelectSpotsViewModel
import com.napptilians.features.viewmodel.SelectDurationViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun registerViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServicesViewModel::class)
    internal abstract fun servicesViewModel(viewModel: ServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddServiceViewModel::class)
    internal abstract fun addServiceViewModel(viewModel: AddServiceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectSpotsViewModel::class)
    internal abstract fun selectSpotsViewModel(viewModel: SelectSpotsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectDurationViewModel::class)
    internal abstract fun selectDurationViewModel(viewModel: SelectDurationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel
}
