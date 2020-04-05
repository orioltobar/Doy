package com.orioltobar.androidklean.view.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orioltobar.androidklean.R
import com.orioltobar.androidklean.base.BaseFragment
import com.orioltobar.commons.error.ErrorModel

class DiscoverFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.discover_fragment, container, false)

    override fun onError(error: ErrorModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}