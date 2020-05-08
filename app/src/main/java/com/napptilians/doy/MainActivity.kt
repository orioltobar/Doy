package com.napptilians.doy

import android.app.NotificationManager
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.napptilians.doy.base.BaseActivity
import com.napptilians.doy.behaviours.ToolbarBehaviour
import com.napptilians.doy.extensions.gone
import com.napptilians.doy.extensions.visible
import com.napptilians.doy.util.Notifications
import kotlinx.android.synthetic.main.activity_main.navBottom
import kotlinx.android.synthetic.main.activity_main.navFragment
import kotlinx.android.synthetic.main.toolbar.toolbar

class MainActivity : BaseActivity(), ToolbarBehaviour {

    override val layoutId: Int get() = R.layout.activity_main
    override val genericToolbar: Toolbar? get() = toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set main theme after splash.
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        initNotifications()

        val navHostFragment = NavHostFragment.findNavController(navFragment)
        navHostFragment.addOnDestinationChangedListener { _, destination, args ->
            // Manage navigation bar visibility
            when (destination.id) {
                R.id.addServiceFragment, R.id.chatListFragment, R.id.profileFragment -> {
                    navBottom.visible()
                }
                R.id.categoryListFragment -> {
                    if (args?.get("isAddingService") == true) {
                        navBottom.gone()
                    } else {
                        navBottom.visible()
                    }
                }
                R.id.eventsFragment -> {
                    if (args?.get("onlyMyEvents") == true) {
                        navBottom.gone()
                    } else {
                        navBottom.visible()
                    }
                }
                else -> {
                    navBottom.gone()
                }
            }
            // Manage toolbar visibility, make on post to wait for the view to be ready
            Handler(Looper.getMainLooper()).post {
                when (destination.id) {
                    R.id.introFragment, R.id.addServiceFragment,
                    R.id.chatListFragment -> {
                        disableToolbar()
                    }
                    R.id.categoryListFragment -> {
                        if (args?.get("isAddingService") == true) {
                            enableHomeAsUp(true) { navHostFragment.popBackStack() }
                        } else {
                            disableToolbar()
                        }
                    }
                    R.id.eventsFragment -> {
                        if (args?.get("onlyMyEvents") == true) {
                            enableHomeAsUp(true) { navHostFragment.popBackStack() }
                        } else {
                            disableToolbar()
                        }
                    }
                    R.id.loginFragment, R.id.serviceDetailFragment -> {
                        enableHomeAsUp(false) { navHostFragment.popBackStack() }
                    }
                    else -> {
                        enableHomeAsUp(true) { navHostFragment.popBackStack() }
                    }
                }
            }
        }
        NavigationUI.setupWithNavController(navBottom, navHostFragment)
    }

    private fun initNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Notifications.NOTIFICATIONS_CHANNEL_ID.isNotEmpty()) {
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = manager.getNotificationChannel(Notifications.NOTIFICATIONS_CHANNEL_ID)
                if (channel?.importance != NotificationManager.IMPORTANCE_NONE) {
                    Notifications.createChannel(
                        this,
                        Notifications.NOTIFICATIONS_CHANNEL_ID,
                        getString(R.string.event_reminder_channel_name),
                        getString(R.string.event_reminder_channel_description),
                        NotificationManager.IMPORTANCE_HIGH
                    )
                }
            }
        }
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
