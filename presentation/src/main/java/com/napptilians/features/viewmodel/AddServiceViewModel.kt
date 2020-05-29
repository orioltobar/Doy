package com.napptilians.features.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.either
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.domain.usecases.AddServiceUseCase
import com.napptilians.domain.usecases.GetUserUseCase
import com.napptilians.features.Error
import com.napptilians.features.NewValue
import com.napptilians.features.UiStatus
import com.napptilians.features.base.BaseViewModel
import com.napptilians.features.base.SingleLiveEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@ExperimentalCoroutinesApi
class AddServiceViewModel @Inject constructor(
    private val addServiceUseCase: AddServiceUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<AddServiceViewModel>() {

    private val _addServiceDataStream = MutableLiveData<UiStatus<Long, ErrorModel>>()
    val addServiceDataStream: LiveData<UiStatus<Long, ErrorModel>>
        get() = _addServiceDataStream

    private val _userDataStream = SingleLiveEvent<UiStatus<ChatRequestModel, ErrorModel>>()
    val userDataStream: LiveData<UiStatus<ChatRequestModel, ErrorModel>> get() = _userDataStream

    var service = ServiceModel()
    val serviceCategory = MutableLiveData("")
    val serviceName = MutableLiveData("")
    val serviceDay = MutableLiveData("")
    val serviceDate = MutableLiveData("")
    val serviceSpots = MutableLiveData("")
    val serviceDuration = MutableLiveData("")
    val serviceDescription = MutableLiveData("")
    val isValidService = MediatorLiveData<Boolean>()

    init {
        isValidService.apply {
            addSource(serviceCategory) {
                isValidService.value = isFormValid(service)
            }
            addSource(serviceName) {
                service.name = serviceName.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDay) {
                service.day = serviceDay.value
                service.date = buildDate(service.day, service.hour)
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDate) {
                service.hour = serviceDate.value
                service.date = buildDate(service.day, service.hour)
                isValidService.value = isFormValid(service)
            }
            addSource(serviceDescription) {
                service.description = serviceDescription.value
                isValidService.value = isFormValid(service)
            }
            addSource(serviceSpots) {
                service.spots = serviceSpots.value?.toIntOrNull()
                isValidService.value = isFormValid(service)
            }
        }
    }

    fun updateDuration(duration: Int) {
        service.durationMin = duration
        isValidService.value = isFormValid(service)
    }

    private fun isFormValid(service: ServiceModel): Boolean =
        service.categoryId != null
                && service.spots != null
                && service.durationMin != null
                && !service.name.isNullOrBlank()
                && !service.day.isNullOrBlank()
                && !service.hour.isNullOrBlank()
                && !service.description.isNullOrBlank()
                && isDateInTheFuture(service.date)

    private fun isDateInTheFuture(date: ZonedDateTime?): Boolean {
        val currentDate = Instant.now().atZone(ZoneId.systemDefault())
        return date?.isAfter(currentDate) == true
    }

    fun execute() {
        viewModelScope.launch {
            _addServiceDataStream.value = emitLoadingState()
            service.ownerId = firebaseAuth.currentUser?.uid ?: ""
            val request = addServiceUseCase.execute(service)
            _addServiceDataStream.value = processModel(request)
        }
    }

    fun executeGetChatInformation(
        serviceId: Long,
        serviceName: String,
        serviceStartDate: ZonedDateTime
    ) {
        viewModelScope.launch {
            _userDataStream.setValue(emitLoadingState())
            val currentUserRequest = getUserUseCase(firebaseAuth.uid ?: "")
            currentUserRequest.either(
                onSuccess = { userModel ->
                    val requestModel = ChatRequestModel(
                        userModel.id,
                        serviceId,
                        userModel.name,
                        serviceName,
                        serviceStartDate
                    )
                    _userDataStream.setValue(NewValue(requestModel))
                },
                onFailure = {
                    _userDataStream.setValue(Error(ErrorModel("")))
                }
            )
        }
    }

    private fun buildDate(day: String?, hour: String?): ZonedDateTime? {
        return if (day == null || hour == null) {
            null
        } else {
            val dateString = "${day}T${hour}"
            try {
                LocalDateTime.parse(dateString).atZone(ZoneId.systemDefault())
            } catch (e: Exception) {
                Log.d(TAG, "There was an error parsing: $dateString")
                null
            }
        }
    }

    companion object {
        private const val TAG = "AddServiceViewModel"
    }
}
