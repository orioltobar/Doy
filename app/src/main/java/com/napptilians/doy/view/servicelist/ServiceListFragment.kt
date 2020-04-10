package com.napptilians.doy.view.servicelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.movie.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ServicesViewModel
import javax.inject.Inject
import kotlinx.android.synthetic.main.service_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ServiceListFragment : BaseFragment() {

    private val viewModel: ServicesViewModel by viewModels { vmFactory }
    private val args: ServiceListFragmentArgs by navArgs()

    @Inject
    lateinit var servicesAdapter: ServiceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.service_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.execute(args.categoryId)
        viewModel.servicesDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<List<ServiceModel>, ErrorModel>> {
                handleUiStates(
                    it,
                    ::processNewValue
                )
            })
    }

    private fun processNewValue(model: List<ServiceModel>) {
        if (model.isNotEmpty()) {
            servicesAdapter.updateItems(model)
            serviceList.visible()
            loadingProgress.gone()
            loadingText.gone()
        } else {
            serviceList.visible()
            loadingProgress.gone()
            loadingText.text = resources.getString(R.string.no_services)
            loadingText.visible()
        }
    }

    override fun onError(error: ErrorModel) {
        serviceList.gone()
        loadingProgress.gone()
        loadingText.text = resources.getString(R.string.generic_error)
        loadingText.visible()
    }

    override fun onLoading() {
        serviceList.gone()
        loadingProgress.visible()
        loadingText.visible()
    }

    private fun initViews() {
        args.name.takeUnless { it.isEmpty() }?.let { name ->
            titleText.text = name
            titleText.visible()
        }
        val layoutManager = LinearLayoutManager(context)
        serviceList.layoutManager = layoutManager
        servicesAdapter = ServiceListAdapter()
        servicesAdapter.setOnClickListener {
            // TODO: Navigate to Service list screen
            Toast.makeText(
                context,
                "Servei ${it.name}",
                Toast.LENGTH_LONG
            ).show()
        }
//        val  dividerItemDecoration =  DividerItemDecoration(context, layoutManager.orientation)
//        serviceList.addItemDecoration(dividerItemDecoration)
        serviceList.adapter = servicesAdapter
    }
}
