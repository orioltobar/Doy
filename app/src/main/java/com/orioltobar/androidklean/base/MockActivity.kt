package com.orioltobar.androidklean.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orioltobar.androidklean.R
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

/**
 * Activity provided to run instrumented unit tests for custom view.
 */
class MockActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)
    }

    fun setView(
        view: View, layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            MATCH_PARENT, WRAP_CONTENT
        )
    ) {
        findViewById<FrameLayout>(android.R.id.content)
            .also { it.removeAllViews() }
            .addView(view, 0, layoutParams)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}
