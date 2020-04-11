package com.napptilians.doy.view.servicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.service.ServiceModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.features.viewmodel.ServiceDetailViewModel
import kotlinx.android.synthetic.main.service_detail_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.format.DateTimeFormatter
import java.util.*

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
                .load(image)
                .into(toolbarImage)
            serviceDetailTitle.text = name
            serviceDetailDescription.text = description
            setDate(this)
            serviceDetailDuration.text = "$durationMin min"
            serviceDetailSpots.text = "${spots ?: 0}"
        }
    }

    private fun setDate(model: ServiceModel) {
        if (model.day.isNullOrEmpty() || model.date == null) {
            return
        }
        val formatterUserFriendly = DateTimeFormatter.ofPattern(
            DATE_FORMAT_USER,
            Locale(Locale.getDefault().language, Locale.getDefault().country)
        )
        serviceDetailDate.text = formatterUserFriendly.format(model.date).capitalize()
    }

    override fun onError(error: ErrorModel) {
    }

    override fun onLoading() {
    }

    companion object {
        private const val DATE_FORMAT_USER = "EEEE d MMM, k:mm"
    }
}
