package com.napptilians.doy.base

import com.napptilians.doy.di.ViewModelFactory
import com.napptilians.commons.error.ErrorModel
import com.napptilians.features.Error
import com.napptilians.features.Loading
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var vmFactory: ViewModelFactory

    fun <T> handleUiStates(
        status: UiStatus<T, ErrorModel>,
        success: (T) -> Unit
    ) =
        when (status) {
            is Loading -> {
                onLoading()
            }
            is NewValue -> success.invoke(status.result)
            is Error -> onError(status.error)
        }

    abstract fun onError(error: ErrorModel)

    abstract fun onLoading()
}