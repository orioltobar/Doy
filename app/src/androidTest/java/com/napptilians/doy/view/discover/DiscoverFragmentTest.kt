package com.napptilians.doy.view.discover

import androidx.test.rule.ActivityTestRule
import com.napptilians.doy.R
import com.napptilians.doy.UiAssertions
import com.napptilians.doy.base.MockActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscoverFragmentTest : UiAssertions {

    private lateinit var discoverFragment: DiscoverFragment

    @get:Rule
    val activityRule =
        object : ActivityTestRule<MockActivity>(MockActivity::class.java, false, false) {
            override fun afterActivityLaunched() {
                super.afterActivityLaunched()
                runOnUiThread {
                    discoverFragment = DiscoverFragment()
                    activity.setFragment(discoverFragment)
                }
            }
        }

    @Before
    fun setup() {
        activityRule.launchActivity(null)
    }

    @Test
    fun loadingAnimationIsShownByDefaultTest() {
        with(activityRule.activity) {
            checkViewIsDisplayed(R.id.discoverFragmentProgressBar)
            val text =
                activityRule.activity.resources.getText(R.string.wip).toString()
            checkTextIsDisplayed(text)
        }
    }
}
