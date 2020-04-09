package com.napptilians.doy.view.addservice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.features.viewmodel.AddServiceViewModel
import kotlinx.android.synthetic.main.add_service_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddServiceFragment : BaseFragment() {

    private val viewModel: AddServiceViewModel by viewModels { vmFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.add_service_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createEventButton.setOnClickListener {
            viewModel.execute()
            viewModel.addServiceDataStream.observe(
                viewLifecycleOwner,
                Observer { handleUiStates(it, ::processNewValue) })
        }
    }

    private fun processNewValue(serviceId: Long) {
        // TODO: Store id on the service or what?
        Log.d("cacatua", "se ha creado un id del servicio: $serviceId")
    }

    override fun onLoading() {
    }

    override fun onError(error: ErrorModel) {
    }
}