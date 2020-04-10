package com.napptilians.doy

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.napptilians.doy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val layoutId: Int get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set main theme after splash.
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        // Remove action bar
        this.supportActionBar?.hide()

        val navHostFragment = NavHostFragment.findNavController(navFragment)
        navHostFragment.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.introFragment, R.id.splashFragment, R.id.loginFragment, R.id.registerFragment -> {
                    navBottom.visibility = View.GONE
                }
                else -> {
                    navBottom.visibility = View.VISIBLE
                }
            }
        }
        NavigationUI.setupWithNavController(navBottom, navHostFragment)
    }
}
