package com.napptilians.doy.view.servicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.invisible
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.viewmodel.ServiceDetailViewModel
import kotlinx.android.synthetic.main.service_detail_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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
        setupListeners()
    }

    private fun initViews() {
        with(args.service) {
            Glide.with(toolbarImage)
                .load(image)
                .into(toolbarImage)
            serviceDetailTitle.text = name
            serviceDetailDescription.text = description
            setDate(date)
            serviceDetailDuration.text = "$durationMin min"
            serviceDetailSpots.text = "${spots ?: 0}"
            if (assistance) {
                confirmAssistanceButton.invisible()
                cancelAssistanceView.visible()
            } else {
                confirmAssistanceButton.visible()
                cancelAssistanceView.gone()
            }
        }
    }

    private fun setDate(date: ZonedDateTime?) {
        date?.let {
            val formatterUserFriendly = DateTimeFormatter.ofPattern(
                DATE_FORMAT_USER,
                Locale(Locale.getDefault().language, Locale.getDefault().country)
            )
            serviceDetailDate.text = formatterUserFriendly.format(date).capitalize()
        }
    }

    private fun setupListeners() {
        confirmAssistanceButton.setOnClickListener {
            viewModel.executeAdd(args.service.serviceId ?: -1L)
            viewModel.addAttendeeServiceDataStream.observe(
                viewLifecycleOwner,
                Observer { handleUiStates(it, ::processConfirmAssistNewValue) })
        }
        cancelAssistanceButton.setOnClickListener {
            viewModel.executeDelete(args.service.serviceId ?: -1L)
            viewModel.deleteAttendeeServiceDataStream.observe(
                viewLifecycleOwner,
                Observer { handleUiStates(it, ::processCancelAssistNewValue) })
        }
    }

    private fun processConfirmAssistNewValue(unit: Unit) {
        progressBar.gone()
        activity?.let { activity ->
            DoyDialog(activity).apply {
                setPopupIcon(R.drawable.ic_thumb_up)
                setPopupTitle(context.resources.getString(R.string.add_attendee_success))
                setPopupSubtitle(context.resources.getString(R.string.add_attendee_success_message))
                show()
            }
        }
        confirmAssistanceButton.invisible()
        cancelAssistanceView.visible()
    }

    private fun processCancelAssistNewValue(unit: Unit) {
        progressBar.gone()
        confirmAssistanceButton.visible()
        cancelAssistanceView.gone()
    }

    override fun onError(error: ErrorModel) {
        progressBar.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        progressBar.visible()
    }

    companion object {
        private const val DATE_FORMAT_USER = "EEEE d MMM, k:mm"
    }
}
