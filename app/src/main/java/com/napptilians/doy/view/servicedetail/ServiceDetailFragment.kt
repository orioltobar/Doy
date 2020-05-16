package com.napptilians.doy.view.servicedetail

import android.app.AlarmManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.invisible
import com.napptilians.doy.extensions.marginPx
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.HourFormatter
import com.napptilians.doy.util.Notifications
import com.napptilians.doy.view.customviews.CancelAssistDialog
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.ServiceDetailViewModel
import kotlinx.android.synthetic.main.service_detail_fragment.appBar
import kotlinx.android.synthetic.main.service_detail_fragment.cancelAssistanceButton
import kotlinx.android.synthetic.main.service_detail_fragment.cancelAssistanceView
import kotlinx.android.synthetic.main.service_detail_fragment.collapsingToolbar
import kotlinx.android.synthetic.main.service_detail_fragment.confirmAssistanceButton
import kotlinx.android.synthetic.main.service_detail_fragment.progressBar
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailAttendees
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDate
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDescription
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailDuration
import kotlinx.android.synthetic.main.service_detail_fragment.serviceDetailOwner
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

    private val formatter = HourFormatter()

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (appBar?.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            val heightDp = resources.displayMetrics.heightPixels * APP_BAR_PERCENTAGE_HEIGHT
            height = heightDp.toInt()
        }
    }

    private fun initToolbar() {
        context?.let {
            toolbar?.navigationIcon = it.getDrawable(R.drawable.ic_back_white_shadow)
            toolbar?.setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initObservers() {
        viewModel.addAttendeeServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processConfirmAssistNewValue) })
        viewModel.deleteAttendeeServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processCancelAssistNewValue) })
        // SingleLiveEvent Observer
        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<ChatRequestModel, ErrorModel>> { status ->
                handleUiStates(status) { scheduleNotification(it) }
            }
        )
    }

    private fun initViews() {
        // Disable scroll of App Bar only: https://stackoverflow.com/a/40750707
        (appBar.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
            val heightDp = resources.displayMetrics.heightPixels * APP_BAR_PERCENTAGE_HEIGHT
            height = heightDp.toInt()
            behavior = AppBarLayout.Behavior().apply {
                setDragCallback(object : DragCallback() {
                    override fun canDrag(@NonNull appBarLayout: AppBarLayout): Boolean = false
                })
            }
        }

        // Init views that require service detail data
        with(args.service) {
            Glide.with(toolbarImage)
                .load(image)
                .into(toolbarImage)
            Glide.with(serviceOwnerImage)
                .load(ownerImage)
                .placeholder(R.drawable.ic_service_owner_placeholder)
                .transform(CircleCrop())
                .into(serviceOwnerImage)
            serviceDetailTitle.text = name
            serviceDetailDescription.text = description
            setDate(date)
            serviceDetailDuration.text = formatter.formatHour(context, durationMin ?: 0)
            serviceDetailSpots.text = "${spots ?: 0}"
            setAttendees(attendees)
            if (ownerId?.equals(firebaseAuth.currentUser?.uid) == true) {
                serviceDetailOwner.visible()
                serviceDetailAttendees.marginPx(bottom = 0)
                confirmAssistanceButton.gone()
                cancelAssistanceView.gone()
            } else {
                serviceDetailOwner.gone()
                serviceDetailAttendees.marginPx(bottom = resources.getDimension(R.dimen.margin_space_bottom).toInt())
                if (assistance) {
                    confirmAssistanceButton.gone()
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
        appBar.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            val currentHeight = collapsingToolbar.height + verticalOffset
            val collapsedHeight = ViewCompat.getMinimumHeight(collapsingToolbar)
            val startCollapsingHeight = 2 * collapsedHeight
            context?.let {
                when {
                    currentHeight <= collapsedHeight -> {
                        // Collapsed
                        toolbar?.navigationIcon = it.getDrawable(R.drawable.ic_back_white_no_shadow)
                    }
                    currentHeight < startCollapsingHeight -> {
                        // Between collapsed and middle
                        val opacity = MAX_ALPHA * currentHeight / startCollapsingHeight
                        val alpha = ((MAX_ALPHA - opacity) * ALPHA_OFFSET).toInt()
                        toolbar?.navigationIcon = it.getDrawable(R.drawable.ic_back_white_shadow)
                        toolbar?.navigationIcon?.colorFilter =
                            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                                ColorUtils.setAlphaComponent(
                                    ContextCompat.getColor(it, R.color.white),
                                    alpha
                                ),
                                BlendModeCompat.SRC_ATOP
                            )
                    }
                    else -> {
                        // Between middle and expanded
                        toolbar?.navigationIcon = it.getDrawable(R.drawable.ic_back_white_shadow)
                    }
                }
            }
        })
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
        viewModel.executeGetChatInformation(
            args.service.serviceId ?: -1L,
            args.service.name ?: ""
        )
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
        progressBar.invisible()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onLoading() {
        progressBar.visible()
    }

    private fun scheduleNotification(requestModel: ChatRequestModel) {
        context?.let {
            val alarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val title = serviceDetailTitle.text.toString()
            val subtitle = getString(
                R.string.event_reminder_subtitle,
                Notifications.MINUTES
            )
            val pendingIntent =
                Notifications.preparePendingIntent(
                    it,
                    args.service.serviceId?.toInt() ?: 0,
                    title,
                    subtitle,
                    requestModel
                )
            args.service.date?.let { date ->
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    date.toInstant().toEpochMilli().minus(1000 * 60 * Notifications.MINUTES),
                    pendingIntent
                )
            }
        }
    }

    private fun cancelNotification() {
        context?.let {
            val alarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val title = serviceDetailTitle.text.toString()
            val subtitle = getString(
                R.string.event_reminder_subtitle,
                Notifications.MINUTES
            )
            val pendingIntent =
                Notifications.preparePendingIntent(
                    it,
                    args.service.serviceId?.toInt() ?: 0,
                    title,
                    subtitle,
                    null
                )
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        private const val DATE_FORMAT_USER = "EEEE d MMM, k:mm"
        private const val MAX_ALPHA = 255
        private const val ALPHA_OFFSET = 0.85
        private const val APP_BAR_PERCENTAGE_HEIGHT = 0.35
    }
}
