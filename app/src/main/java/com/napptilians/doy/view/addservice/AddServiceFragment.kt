package com.napptilians.doy.view.addservice

import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.napptilians.commons.error.ErrorModel
import com.napptilians.domain.models.chat.ChatRequestModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.databinding.AddServiceFragmentBinding
import com.napptilians.doy.extensions.decodeByteArrayFromBase64
import com.napptilians.doy.extensions.encodeByteArrayToBase64
import com.napptilians.doy.extensions.getNavigationResult
import com.napptilians.doy.extensions.invisible
import com.napptilians.doy.extensions.resize
import com.napptilians.doy.extensions.toByteArray
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.HourFormatter
import com.napptilians.doy.util.Notifications
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.UiStatus
import com.napptilians.features.viewmodel.AddServiceViewModel
import kotlinx.android.synthetic.main.add_service_fragment.createEventButton
import kotlinx.android.synthetic.main.add_service_fragment.progressBar
import kotlinx.android.synthetic.main.add_service_fragment.selectCategoryEditText
import kotlinx.android.synthetic.main.add_service_fragment.selectDayEditText
import kotlinx.android.synthetic.main.add_service_fragment.selectDurationEditText
import kotlinx.android.synthetic.main.add_service_fragment.selectSpotsEditText
import kotlinx.android.synthetic.main.add_service_fragment.selectTimeEditText
import kotlinx.android.synthetic.main.add_service_fragment.serviceImageView
import kotlinx.android.synthetic.main.add_service_fragment.uploadImageBox
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class AddServiceFragment : BaseFragment() {

    private val viewModel: AddServiceViewModel by viewModels { vmFactory }

    private val serviceDateFormat = SimpleDateFormat(SERVICE_DATE_FORMAT, Locale.getDefault())
    private var selectedCalendarDay: Calendar? = null
    private var selectedTime: String? = null

    private lateinit var alarmManager: AlarmManager
    private var pendingIntent: PendingIntent? = null

    private val formatter = HourFormatter()

    private var serviceId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddServiceFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_service_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        setupListeners()
    }

    private fun initObservers() {
        getNavigationResult("selectCategoryId")?.observe(
            viewLifecycleOwner,
            Observer<String> { viewModel.service.categoryId = it.toLong() }
        )
        getNavigationResult("selectCategoryName")?.observe(
            viewLifecycleOwner,
            Observer<String> { selectCategoryEditText.setText(it) }
        )
        getNavigationResult("selectedDuration")?.observe(
            viewLifecycleOwner,
            Observer<String> {
                selectDurationEditText.setText(formatter.formatHour(context, it.toInt()))
                viewModel.updateDuration(it.toInt())
            }
        )
        getNavigationResult("selectedSpots")?.observe(
            viewLifecycleOwner,
            Observer<String> { selectSpotsEditText.setText(it) }
        )
        viewModel.addServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processNewValue) })
        // SingleLiveEvent Observer
        viewModel.userDataStream.observe(
            viewLifecycleOwner,
            Observer<UiStatus<ChatRequestModel, ErrorModel>> { status ->
                handleUiStates(status) { scheduleNotification(it) }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        Glide.with(serviceImageView)
            .load(viewModel.service.image?.decodeByteArrayFromBase64())
            .into(serviceImageView)
    }

    private fun setupListeners() {
        uploadImageBox.setOnClickListener { openGallery() }
        selectCategoryEditText.setOnClickListener {
            val direction =
                AddServiceFragmentDirections.actionAddServiceFragmentToCategoriesFragment()
            findNavController().navigate(direction)
        }
        selectDayEditText.setOnClickListener { showServiceDayPicker() }
        selectTimeEditText.setOnClickListener { showServiceTimePicker() }
        selectSpotsEditText.setOnClickListener {
            val direction =
                AddServiceFragmentDirections.actionAddServiceFragmentToSelectSpotsFragment()
            findNavController().navigate(direction)
        }
        selectDurationEditText.setOnClickListener {
            val direction =
                AddServiceFragmentDirections.actionAddServiceFragmentToSelectDurationFragment()
            findNavController().navigate(direction)
        }
        createEventButton.setOnClickListener {
            if (!progressBar.isVisible) {
                createEvent()
            }
        }
    }

    private fun openGallery() {
        activity?.let {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    private fun updateServiceDay() {
        selectedCalendarDay?.let {
            selectDayEditText.setText(serviceDateFormat.format(it.time))
        }
    }

    private fun updateServiceTime() {
        selectedTime?.let {
            selectTimeEditText.setText(it)
        }
    }

    private fun showServiceDayPicker() {
        selectedCalendarDay = Calendar.getInstance()
        activity?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    selectedCalendarDay?.set(year, monthOfYear, dayOfMonth)
                    updateServiceDay()
                },
                DEFAULT_SERVICE_DATE_YEAR,
                DEFAULT_SERVICE_DATE_MONTH,
                DEFAULT_SERVICE_DATE_DAY
            ).show()
        }
    }

    private fun showServiceTimePicker() {
        activity?.let {
            TimePickerDialog(
                it,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    updateServiceTime()
                },
                DEFAULT_SERVICE_DATE_HOUR,
                DEFAULT_SERVICE_DATE_MINUTE,
                true
            ).show()
        }
    }

    private fun createEvent() {
        viewModel.execute()
    }

    private fun processNewValue(serviceId: Long) {
        this.serviceId = serviceId
        viewModel.executeGetChatInformation(
            this.serviceId,
            viewModel.service.name ?: "",
            viewModel.service.date ?: ZonedDateTime.now(),
            viewModel.service.durationMin ?: 0
        )
        progressBar.invisible()
        activity?.let { activity ->
            DoyDialog(activity).apply {
                setPopupIcon(R.drawable.ic_thumb_up)
                setPopupTitle(context.resources.getString(R.string.add_service_success))
                setPopupSubtitle(context.resources.getString(R.string.add_service_success_message))
                show()
                setOnDismissListener { findNavController().popBackStack() }
            }
        }
    }

    override fun onLoading() {
        progressBar.visible()
    }

    override fun onError(error: ErrorModel) {
        progressBar.invisible()
        activity?.let { DoyErrorDialog(it).show() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                data?.data?.let { imageUri ->
                    val imageBitmap = BitmapFactory.decodeStream(
                        context?.contentResolver?.openInputStream(imageUri)
                    )?.resize()
                    viewModel.service.image = imageBitmap?.toByteArray()?.encodeByteArrayToBase64()
                }
            }
        }
    }

    private fun scheduleNotification(chatRequestModel: ChatRequestModel) {
        context?.let {
            alarmManager = it.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val title = viewModel.service.name ?: ""
            val subtitle = getString(
                R.string.event_reminder_subtitle,
                Notifications.MINUTES
            )
            pendingIntent =
                Notifications.preparePendingIntent(
                    it,
                    serviceId.toInt(),
                    title,
                    subtitle,
                    chatRequestModel
                )
            viewModel.service.date?.let { date ->
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    date.toInstant().toEpochMilli().minus(1000 * 60 * Notifications.MINUTES),
                    pendingIntent
                )
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val SERVICE_DATE_FORMAT = "yyyy-MM-dd"
        private val calendar = Calendar.getInstance()
        private val DEFAULT_SERVICE_DATE_YEAR = calendar.get(Calendar.YEAR)
        private val DEFAULT_SERVICE_DATE_MONTH = calendar.get(Calendar.MONTH)
        private val DEFAULT_SERVICE_DATE_DAY = calendar.get(Calendar.DAY_OF_MONTH)
        private val DEFAULT_SERVICE_DATE_HOUR = calendar.get(Calendar.HOUR_OF_DAY)
        private val DEFAULT_SERVICE_DATE_MINUTE = calendar.get(Calendar.MINUTE)
    }
}
