package com.napptilians.doy.view.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.napptilians.commons.error.ErrorModel
import com.napptilians.doy.R
import com.napptilians.doy.base.BaseFragment
import kotlinx.android.synthetic.main.discover_fragment.*
import javax.inject.Inject

class DiscoverFragment : BaseFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.discover_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Temp. Remove.
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
        }

        categoryListButton.setOnClickListener {
            val navigation = DiscoverFragmentDirections.actionMenuFavouritesListButtonToCategoryListFragment()
            findNavController().navigate(navigation)
        }
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