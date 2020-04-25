package com.napptilians.doy.view.addservice

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.databinding.AddServiceFragmentBinding
import com.napptilians.doy.extensions.decodeByteArrayFromBase64
import com.napptilians.doy.extensions.encodeByteArrayToBase64
import com.napptilians.doy.extensions.getNavigationResult
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.resize
import com.napptilians.doy.extensions.toByteArray
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.view.customviews.DoyDialog
import com.napptilians.doy.view.customviews.DoyErrorDialog
import com.napptilians.features.viewmodel.AddServiceViewModel
import kotlinx.android.synthetic.main.add_service_fragment.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@ExperimentalCoroutinesApi
class AddServiceFragment : BaseFragment(), ToolbarBehaviour {

    override val genericToolbar: Toolbar? by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }

    private val viewModel: AddServiceViewModel by viewModels { vmFactory }

    private val serviceDateFormat = SimpleDateFormat(SERVICE_DATE_FORMAT, Locale.getDefault())
    private var selectedCalendarDay: Calendar? = null
    private var selectedTime: String? = null

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
        setupSharedObservers()
        setupListeners()
    }

    private fun setupSharedObservers() {
        getNavigationResult("selectCategoryId")?.observe(
            viewLifecycleOwner,
            Observer<String> { viewModel.service.categoryId = it.toLong() }
        )
        getNavigationResult("selectCategoryName")?.observe(
            viewLifecycleOwner,
            Observer<String> { selectCategoryEditText.setText(it) }
        )
        getNavigationResult("selectedSpots")?.observe(
            viewLifecycleOwner,
            Observer<String> { selectSpotsEditText.setText(it) }
        )
        getNavigationResult("selectedDuration")?.observe(
            viewLifecycleOwner,
            Observer<String> {
                if (it.toInt() == 1) {
                    selectDurationEditText.setText("$it ${context?.getString(R.string.hour)}")
                } else {
                    selectDurationEditText.setText("$it ${context?.getString(R.string.hours)}")
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        genericToolbar?.gone()
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
        createEventButton.setOnClickListener { createEvent() }
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
        viewModel.addServiceDataStream.observe(
            viewLifecycleOwner,
            Observer { handleUiStates(it, ::processNewValue) })
    }

    private fun processNewValue(serviceId: Long) {
        progressBar.gone()
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
        progressBar.gone()
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
