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
import com.napptilians.doy.view.customviews.DoyDialog
import kotlinx.android.synthetic.main.discover_fragment.addServiceButton
import kotlinx.android.synthetic.main.discover_fragment.categoryListButton
import kotlinx.android.synthetic.main.discover_fragment.chatsButton
import kotlinx.android.synthetic.main.discover_fragment.detailButton
import kotlinx.android.synthetic.main.discover_fragment.discoverUserUid
import kotlinx.android.synthetic.main.discover_fragment.loginFlowButton
import kotlinx.android.synthetic.main.discover_fragment.popupButton
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
        loginFlowButton.setOnClickListener {
            firebaseAuth.signOut()
            val direction =
                DiscoverFragmentDirections.actionMenuFavouritesListButtonToIntroFragment()
            findNavController().navigate(direction)
        }

        popupButton.setOnClickListener {
            activity?.let { activity ->
                DoyDialog(activity).apply {
                    setPopupIcon(R.drawable.ic_rocket)
                    setPopupTitle(context.resources.getString(R.string.wip))
                    setPopupSubtitle(context.resources.getString(R.string.wip_explanation))
                    setPopupFooterMessage("Empieza a disfrutar de formar parte de la comunidad DOY.")
                    setPopupFooterImage(R.drawable.popup_footer)
                    show()
                }
            }
        }
        categoryListButton.setOnClickListener {
            val navigation =
                DiscoverFragmentDirections.actionMenuFavouritesListButtonToCategoryListFragment()
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
        addServiceButton.setOnClickListener {
            val navigation =
                DiscoverFragmentDirections.actionMenuFavouritesListButtonToAddServiceFragment()
            findNavController().navigate(navigation)
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.currentUser?.let {
            discoverUserUid.text = it.uid
        }
    }

    override fun onError(error: ErrorModel) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoading() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
