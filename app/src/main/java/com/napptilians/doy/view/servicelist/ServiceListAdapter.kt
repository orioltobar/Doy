package com.napptilians.doy.view.servicelist

import android.view.ViewGroup
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.base.BaseAdapter
import javax.inject.Inject

class ServiceListAdapter @Inject constructor() : BaseAdapter<ServiceModel, ServiceItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceItemViewHolder {
        return ServiceItemViewHolder(parent)
    }
}
