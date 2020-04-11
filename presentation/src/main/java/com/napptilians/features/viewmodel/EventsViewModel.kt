package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetEventsUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class EventsViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase
) : BaseViewModel<ServiceModel>() {

    private val _eventsDataStream =
        MutableLiveData<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>>()
    val eventsDataStream: LiveData<UiStatus<Map<String, List<ServiceModel>>, ErrorModel>>
        get() = _eventsDataStream

    fun execute(
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
}
