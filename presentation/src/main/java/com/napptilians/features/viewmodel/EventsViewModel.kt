package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetEventsUseCase
import com.napptilians.domain.usecases.GetMyServicesUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val getMyServicesUseCase: GetMyServicesUseCase
) : BaseViewModel<ServiceModel>() {

    private val _eventsDataStream =
        MutableLiveData<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>>()
    val eventsDataStream: LiveData<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>>
        get() = _eventsDataStream

    fun getServices(
        categoryIds: List<Long> = emptyList(),
        serviceId: Long? = null,
        uid: String? = null
    ) {
        viewModelScope.launch {
            _eventsDataStream.value = emitLoadingState()
            val request = getEventsUseCase.execute(categoryIds, serviceId, uid)
            _eventsDataStream.value = processModel(request)
        }
    }

    fun getMyServices(uid: String? = null) {
        viewModelScope.launch {
            _eventsDataStream.value = emitLoadingState()
            val request = getMyServicesUseCase.execute(uid)
            _eventsDataStream.value = processModel(request)
        }
    }
}
