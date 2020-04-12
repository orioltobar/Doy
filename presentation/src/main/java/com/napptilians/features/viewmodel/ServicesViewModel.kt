package com.napptilians.features.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.GetServicesUseCase
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase,
    private val firebaseAuth: FirebaseAuth
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
            val request = getServicesUseCase.execute(categoryIds, serviceId, firebaseAuth.currentUser?.uid)
            _servicesDataStream.value = processModel(request)
        }
    }
}
