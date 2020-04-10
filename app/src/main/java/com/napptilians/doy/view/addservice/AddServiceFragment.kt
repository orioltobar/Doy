package com.napptilians.doy.view.addservice

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import com.napptilians.doy.view.register.RegisterFragmentDirections
import com.napptilians.features.viewmodel.AddServiceViewModel
import kotlinx.android.synthetic.main.add_service_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AddServiceFragment : BaseFragment() {

    private val viewModel: AddServiceViewModel by viewModels { vmFactory }

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.add_service_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        uploadImageBox.setOnClickListener { openGallery() }
        selectCategoryEditText.setOnClickListener {
            val direction = AddServiceFragmentDirections.actionAddServiceFragmentToCategoriesFragment()
            findNavController().navigate(direction)
        }
        selectSpotsEditText.setOnClickListener {  }
        selectDurationEditText.setOnClickListener {  }
        createEventButton.setOnClickListener { createEvent() }
    }

    private fun openGallery() {
        activity?.let {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
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
        progressDialog.cancel()
    }

    override fun onLoading() {
        progressDialog = ProgressDialog.show(
            context,
            "",
            getString(R.string.adding_service),
            true
        )
    }

    override fun onError(error: ErrorModel) {
        progressDialog.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            GALLERY_REQUEST_CODE -> serviceImageView.setImageURI(data?.data)
        }
    }

    companion object {
        private const val GALLERY_REQUEST_CODE = 100
    }
}