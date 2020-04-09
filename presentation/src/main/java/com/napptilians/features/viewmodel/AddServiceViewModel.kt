package com.napptilians.features.viewmodel

import com.napptilians.domain.models.movie.MovieModel
import com.napptilians.domain.usecases.AddServiceUseCase
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddServiceViewModel @Inject constructor(
    private val addServiceUseCase: AddServiceUseCase
) : BaseViewModel<MovieModel>() {
}
