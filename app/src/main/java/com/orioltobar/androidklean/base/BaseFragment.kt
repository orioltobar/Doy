package com.orioltobar.androidklean.base

import com.orioltobar.androidklean.di.ViewModelFactory
import com.orioltobar.commons.error.ErrorModel
import com.orioltobar.features.Error
import com.orioltobar.features.Loading
import com.orioltobar.features.NewValue
import com.orioltobar.features.UiStatus
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