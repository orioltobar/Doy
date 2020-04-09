package com.napptilians.doy.view.addservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment

class AddServiceFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.add_service_fragment, container, false)

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun onError(error: ErrorModel) {
        TODO("Not yet implemented")
    }
}