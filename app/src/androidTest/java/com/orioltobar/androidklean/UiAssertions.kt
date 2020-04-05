package com.orioltobar.androidklean

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

/**
 * Centralizes some useful functions meant to be used at Espresso tests.
 */
interface UiAssertions {

    /**
     * Checks if there is some view with this [text] being displayed on it.
     * This check is not case sensitive (this means that "Log In" equals to "log IN").
     */
    fun checkTextIsDisplayed(text: String) {
        Espresso.onView(ViewMatchers.withText(Matchers.equalToIgnoringCase(text)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Checks if there is a visible view with this [viewId].
     */
    fun checkViewIsDisplayed(viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Checks if there is a hidden view with this [viewId].
     */
    fun checkViewIsNotDisplayed(viewId: Int) {
        Espresso.onView(ViewMatchers.withId(viewId))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }
    
    /**
     * Function that will allow us to check that a certain item from a RecyclerView has a
     * certain text.
     *
     * @param recyclerViewId The id of the [RecyclerView].
     * @param itemPosition The position of the item inside the [RecyclerView].
     * @param text The text to be checked.
     * @param isRoot Indicates whether the view to check is the root one. True if it is the root
     * one. False if it is a descendant.
     */
    fun checkThatRecyclerViewItemHasText(
        recyclerViewId: Int,
        itemPosition: Int,
        text: String?,
        isRoot: Boolean = false
    ) {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemPosition))
        Espresso.onView(RecyclerViewMatcher(recyclerViewId).atPosition(itemPosition))
            .check(
                ViewAssertions.matches(
                    if (isRoot) ViewMatchers.withText(text) else ViewMatchers.hasDescendant(
                        ViewMatchers.withText(text)
                    )
                )
            )
    }

    /**
     * Function that will allow us to click on a desired [RecyclerView] item.
     *
     * @param recyclerViewId The id of the [RecyclerView].
     * @param itemPosition The position of the item inside the [RecyclerView].
     */
    fun performRecyclerViewItemClick(recyclerViewId: Int, itemPosition: Int) {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemPosition))
        Espresso.onView(RecyclerViewMatcher(recyclerViewId).atPosition(itemPosition))
            .perform(ViewActions.click())
    }

    /**
     * Function that will allow us to check that a [RecyclerView] has a certain number of items.
     *
     * @param recyclerViewId The id of the [RecyclerView].
     * @param expectedItemCount The expected quantity of items for the [RecyclerView].
     */
    fun checkRecyclerViewItemCount(recyclerViewId: Int, expectedItemCount: Int) {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(recyclerViewId),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
            .check(itemCountAssertion(expectedItemCount))
    }
}
