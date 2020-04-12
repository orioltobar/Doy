package com.napptilians.doy.behaviours

import androidx.appcompat.widget.Toolbar
import com.napptilians.doy.R

interface ToolbarBehaviour {

    /**
     * Define generic toolbar to be injected in each specific screen.
     */
    val toolbar: Toolbar?

    /**
     * Method used to define the function to invoke when the navigation icon is clicked.
     */
    fun enableHomeAsUp(up: () -> Unit) {
        toolbar?.run {
            navigationIcon = context.getDrawable(R.drawable.ic_back_white)
            setNavigationOnClickListener { up.invoke() }
        }
    }
}
