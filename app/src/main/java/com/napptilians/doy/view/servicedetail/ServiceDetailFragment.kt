package com.napptilians.doy.view.servicedetail

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.invisible
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.Notifications
import com.napptilians.doy.view.customviews.CancelAssistDialog
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.viewmodel.ServiceDetailViewModel
import kotlinx.android.synthetic.main.service_detail_fragment.cancelAssistanceButton
import kotlinx.android.synthetic.main.service_detail_fragment.cancelAssistanceView
import kotlinx.android.synthetic.main.service_detail_fragment.confirmAssistanceButton
import kotlinx.android.synthetic.main.service_detail_fragment.progressBar
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailAttendees
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDate
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDescription
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDuration
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailSpots
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailTitle
import kotlinx.android.synthetic.main.service_detail_fragment.serviceOwnerImage
import kotlinx.android.synthetic.main.service_detail_fragment.toolbar
import kotlinx.android.synthetic.main.service_detail_fragment.toolbarImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ServiceDetailFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: ServiceDetailViewModel by viewModels { vmFactory }
    private val args: ServiceDetailFragmentArgs by navArgs()
    private lateinit var alarmManager: AlarmManager
    private var pendingIntent: PendingIntent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.service_detail_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initObservers()
        initViews()
        setupListeners()
    }

    private fun initToolbar() {
        context?.let {
            toolbar?.navigationIcon = it.getDrawable(R.drawable.ic_back_white)
        }
        toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initObservers() {
        viewModel.addAttendeeServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processConfirmAssistNewValue) })
        viewModel.deleteAttendeeServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processCancelAssistNewValue) })
    }

    private fun initViews() {
        with(args.service) {
            Glide.with(toolbarImage)
                .load(image)
                .into(toolbarImage)
            Glide.with(serviceOwnerImage)
                .load(ownerImage)
                .placeholder(R.drawable.ic_profile)
                .into(serviceOwnerImage)
            serviceDetailTitle.text = name
            serviceDetailDescription.text = description
            setDate(date)
            serviceDetailDuration.text = "$durationMin min"
            serviceDetailSpots.text = "${spots ?: 0}"
            setAttendees(attendees)
            if (ownerId?.equals(firebaseAuth.currentUser?.uid) == true) {
                confirmAssistanceButton.gone()
                cancelAssistanceView.gone()
                context?.let {
                    Toast.makeText(it, it.getString(R.string.your_service), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if (assistance) {
                    confirmAssistanceButton.invisible()
                    cancelAssistanceView.visible()
                } else {
                    confirmAssistanceButton.visible()
                    cancelAssistanceView.gone()
                }
            }
        }
    }

    private fun setAttendees(attendees: Int?) {
        serviceDetailAttendees.text = context?.resources?.getQuantityString(
            R.plurals.spots_reserved,
            attendees ?: 0,
            attendees ?: 0
        )
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
        }
        cancelAssistanceButton.setOnClickListener {
            context?.let { context ->
                CancelAssistDialog(context) { performCancelAssist() }.show()
            }
        }
    }

    private fun performCancelAssist() {
        viewModel.executeDelete(args.service.serviceId ?: -1L)
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
        args.service.attendees = args.service.attendees?.inc()
        setAttendees(args.service.attendees)
        confirmAssistanceButton.invisible()
        cancelAssistanceView.visible()
        scheduleNotification()
    }

    private fun processCancelAssistNewValue(unit: Unit) {
        progressBar.gone()
        args.service.attendees = args.service.attendees?.dec()
        setAttendees(args.service.attendees)
        confirmAssistanceButton.visible()
        cancelAssistanceView.gone()
        cancelNotification()
    }

    override fun onError(error: ErrorModel) {
        progressBar.gone()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        progressBar.visible()
    }

    private fun scheduleNotification() {
        context?.let {
            alarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val title = serviceDetailTitle.text.toString()
            val subtitle = getString(
                R.string.event_reminder_subtitle,
                MINUTES
            )
            val serviceId = args.service.serviceId ?: -1L
            pendingIntent =
                Notifications.preparePendingIntent(
                    it,
                    args.service.serviceId?.toInt() ?: 0,
                    title,
                    subtitle,
                    serviceId
                )
            args.service.date?.let { date ->
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    date.toInstant().toEpochMilli().minus(1000 * 60 * MINUTES),
                    pendingIntent
                )
            }
        }
    }

    private fun cancelNotification() {
        pendingIntent?.let {
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        private const val DATE_FORMAT_USER = "EEEE d MMM, k:mm"
        private const val MINUTES = 5
    }
}
