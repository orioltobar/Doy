package com.orioltobar.androidklean

import android.content.res.Resources
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher class to be used with Espresso in order to match child views of the given [recyclerViewId].
 */
class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> = atPositionOnView(position, -1)

    private fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            private var resources: Resources? = null
            private var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                resources?.let {
                    idDescription = try {
                        it.getResourceName(recyclerViewId)
                    } catch (e: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", recyclerViewId)
                    }
                }
                description.appendText("RecyclerView with id: $idDescription at position: $position")
            }

            public override fun matchesSafely(view: View): Boolean {
                resources = view.resources
                if (childView == null) {
                    val recyclerView =
                        view.rootView.findViewById<androidx.recyclerview.widget.RecyclerView>(
                            recyclerViewId
                        )
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                        viewHolder?.let {
                            childView = viewHolder.itemView
                        }
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView?.findViewById<View>(targetViewId)
                    view === targetView
                }
            }
        }
    }
}
