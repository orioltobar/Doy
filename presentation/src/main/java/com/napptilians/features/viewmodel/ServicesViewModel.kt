package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetServicesUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase
) : BaseViewModel<ServiceModel>() {

    private val _servicesDataStream = MutableLiveData<UiStatus<List<ServiceModel>, ErrorModel>>()
    val servicesDataStream: LiveData<UiStatus<List<ServiceModel>, ErrorModel>>
        get() = _servicesDataStream

    fun execute(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null
    ) {
        viewModelScope.launch {
            _servicesDataStream.value = emitLoadingState()
            val request = getServicesUseCase.execute(categoryIds, serviceId, uid)
            _servicesDataStream.value = processModel(request)
        }
    }
}
