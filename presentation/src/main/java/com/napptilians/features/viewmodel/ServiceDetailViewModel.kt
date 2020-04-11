package com.napptilians.features.viewmodel

import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.features.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServiceDetailViewModel @Inject constructor() : BaseViewModel<ServiceModel>()
