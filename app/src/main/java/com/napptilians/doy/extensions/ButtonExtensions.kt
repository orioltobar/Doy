package com.napptilians.doy.extensions

import android.widget.Button

fun Button.clickable() {
    this.isClickable = true
}

fun Button.notClickable() {
    this.isClickable = false
}
