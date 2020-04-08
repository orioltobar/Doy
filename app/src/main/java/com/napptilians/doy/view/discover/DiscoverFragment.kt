package com.napptilians.doy.view.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.discover_fragment.*

class DiscoverFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.discover_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatsButton.setOnClickListener {
            val direction =
                DiscoverFragmentDirections.actionMenuFavouritesListButtonToChatFragment()
            findNavController().navigate(direction)
        }
        loginFlowButton.setOnClickListener {
            val direction =
                DiscoverFragmentDirections.actionMenuFavouritesListButtonToIntroFragment()
            findNavController().navigate(direction)
        }
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}