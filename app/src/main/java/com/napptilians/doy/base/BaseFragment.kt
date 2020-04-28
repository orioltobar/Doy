package com.napptilians.doy.base

import androidx.appcompat.widget.Toolbar
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.di.ViewModelFactory
import com.napptilians.features.Error
import com.napptilians.features.Loading
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), ToolbarBehaviour {

    override val genericToolbar: Toolbar? by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

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
