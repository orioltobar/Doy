package com.napptilians.doy.view.servicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.decodeByteArrayFromBase64
import com.napptilians.doy.view.categorylist.CategoryListFragmentArgs
import com.napptilians.features.viewmodel.CategoriesViewModel
import com.napptilians.features.viewmodel.ServiceDetailViewModel
import kotlinx.android.synthetic.main.service_detail_fragment.toolbarImage
import kotlinx.android.synthetic.main.service_item.view.serviceImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ServiceDetailFragment : BaseFragment() {

    private val viewModel: ServiceDetailViewModel by viewModels { vmFactory }
    private val args: ServiceDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.service_detail_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(args.service) {
            Glide.with(toolbarImage)
                .load(image?.decodeByteArrayFromBase64())
                .into(toolbarImage)
        }
    }

    override fun onError(error: ErrorModel) {
    }

    override fun onLoading() {
    }
}
