package com.napptilians.doy

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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

    /**
     * This method overrides the default dispatch touch event from the Android [AppCompatActivity].
     * It clears the focus of [EditText] when a touch event is triggered outside.
     *
     * @param event The [MotionEvent] executed by the user when touching the screen
     * @return The [Boolean] with the default behaviour of [dispatchTouchEvent]
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // Clear Edit Text focus if any
        if (event.action == MotionEvent.ACTION_DOWN) {
            (currentFocus as? EditText)?.run {
                val outRect = Rect().apply { getGlobalVisibleRect(this) }
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    clearFocus()
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                        ?.hideSoftInputFromWindow(windowToken, 0)
                }
            }
        }
        // Return default behaviour
        return super.dispatchTouchEvent(event)
    }
}
