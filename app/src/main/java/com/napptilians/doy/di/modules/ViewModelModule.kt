package com.napptilians.doy.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.napptilians.doy.di.ViewModelFactory
import com.napptilians.doy.di.ViewModelKey
import com.napptilians.features.viewmodel.AddServiceViewModel
import com.napptilians.features.viewmodel.CategoriesViewModel
import com.napptilians.features.viewmodel.EventsViewModel
import com.napptilians.features.viewmodel.ChatListViewModel
import com.napptilians.features.viewmodel.ChatViewModel
import com.napptilians.features.viewmodel.LoginViewModel
import com.napptilians.features.viewmodel.MovieViewModel
import com.napptilians.features.viewmodel.ProfileViewModel
import com.napptilians.features.viewmodel.RecoverPasswordViewModel
import com.napptilians.features.viewmodel.ServicesViewModel
import com.napptilians.features.viewmodel.RegisterViewModel
import com.napptilians.features.viewmodel.SelectSpotsViewModel
import com.napptilians.features.viewmodel.SelectDurationViewModel
import com.napptilians.features.viewmodel.ServiceDetailViewModel
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
    @ViewModelKey(RecoverPasswordViewModel::class)
    internal abstract fun recoverPasswordViewModel(viewModel: RecoverPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServicesViewModel::class)
    internal abstract fun servicesViewModel(viewModel: ServicesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ServiceDetailViewModel::class)
    internal abstract fun serviceDetailViewModel(viewModel: ServiceDetailViewModel): ViewModel

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
    @ViewModelKey(EventsViewModel::class)
    internal abstract fun eventsViewModel(viewModel: EventsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatListViewModel::class)
    internal abstract fun chatListViewModel(viewModel: ChatListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    internal abstract fun chatViewModel(viewModel: ChatViewModel): ViewModel
}
