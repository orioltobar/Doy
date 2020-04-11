package com.napptilians.doy.view.addservice

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.databinding.AddServiceFragmentBinding
import com.napptilians.doy.extensions.encodeByteArrayToBase64
import com.napptilians.doy.extensions.getNavigationResult
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.resize
import com.napptilians.doy.extensions.toByteArray
import com.napptilians.doy.extensions.visible
import com.napptilians.features.viewmodel.AddServiceViewModel
import kotlinx.android.synthetic.main.add_service_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@ExperimentalCoroutinesApi
class AddServiceFragment : BaseFragment() {

    private val viewModel: AddServiceViewModel by viewModels { vmFactory }

    private val serviceDateFormat = SimpleDateFormat(SERVICE_DATE_FORMAT, Locale.getDefault())
    private var selectedCalendarDay: Calendar? = null

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
            Observer<String> { selectDurationEditText.setText(it) }
        )
    }

    private fun setupListeners() {
        uploadImageBox.setOnClickListener { openGallery() }
        selectCategoryEditText.setOnClickListener {
            val direction =
                AddServiceFragmentDirections.actionAddServiceFragmentToCategoriesFragment()
            findNavController().navigate(direction)
        }
        selectDateEditText.setOnClickListener { showServiceDatePicker() }
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


    private fun updateServiceDate() {
        selectedCalendarDay?.let {
            viewModel.addServiceDataStream
            selectDateEditText.setText(serviceDateFormat.format(it.time))
        }
    }

    private fun showServiceDatePicker() {
        selectedCalendarDay = Calendar.getInstance()
        activity?.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    selectedCalendarDay?.set(year, monthOfYear, dayOfMonth)
                    updateServiceDate()
                },
                DEFAULT_SERVICE_DATE_YEAR,
                DEFAULT_SERVICE_DATE_MONTH,
                DEFAULT_SERVICE_DATE_DAY
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
        // TODO: Store id on the service or what?
        progressBar.gone()
    }

    override fun onLoading() {
        progressBar.visible()
    }

    override fun onError(error: ErrorModel) {
        progressBar.gone()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                serviceImageView.setImageURI(data?.data)
                viewModel.service.image = BitmapFactory.decodeStream(
                    context!!.contentResolver.openInputStream(data!!.data!!))?.resize()?.toByteArray()?.encodeByteArrayToBase64()
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
        private const val SERVICE_DATE_FORMAT = "yyyy-dd-MM"
        private val calendar = Calendar.getInstance()
        private val DEFAULT_SERVICE_DATE_YEAR = calendar.get(Calendar.YEAR)
        private val DEFAULT_SERVICE_DATE_MONTH = calendar.get(Calendar.MONTH)
        private val DEFAULT_SERVICE_DATE_DAY = calendar.get(Calendar.DAY_OF_MONTH)
    }
}
