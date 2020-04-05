package com.orioltobar.androidklean

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

/**
 * Utility to check if the number of items in a recycler view matches the [expectedCount].
 */
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let { throw it }
        val recyclerView = view as? androidx.recyclerview.widget.RecyclerView
        val adapter = recyclerView?.adapter
        ViewMatchers.assertThat(adapter?.itemCount, CoreMatchers.`is`(expectedCount))
    }
}

fun itemCountAssertion(expectedCount: Int) = RecyclerViewItemCountAssertion(expectedCount)
