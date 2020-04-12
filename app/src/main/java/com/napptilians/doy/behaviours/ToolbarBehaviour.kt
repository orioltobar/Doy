package com.napptilians.doy.behaviours

import androidx.appcompat.widget.Toolbar
import com.napptilians.doy.R
import com.napptilians.doy.extensions.visible

interface ToolbarBehaviour {

    /**
     * Define generic toolbar to be injected in each specific screen.
     */
    val genericToolbar: Toolbar?

    /**
     * Method used to define the function to invoke when the navigation icon is clicked.
     */
    fun enableHomeAsUp(isDark: Boolean = true, up: () -> Unit) {
        genericToolbar?.run {
            visible()
            navigationIcon = if (isDark) {
                context.getDrawable(R.drawable.ic_back)
            } else {
                context.getDrawable(R.drawable.ic_back_white)
            }
            setNavigationOnClickListener { up.invoke() }
        }
    }
}
