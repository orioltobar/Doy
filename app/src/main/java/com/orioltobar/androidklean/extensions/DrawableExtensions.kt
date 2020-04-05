package com.orioltobar.androidklean.extensions

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette

fun Drawable.getDominantColor(): Int {
    val bitmap = (this as BitmapDrawable).bitmap
    val color = 0

    val swatchesTemp = Palette.from(bitmap).generate().swatches
    val swatches = ArrayList(swatchesTemp)
    swatches.sortWith(Comparator { swatch1, swatch2 -> swatch2.population - swatch1.population })
    return if (swatches.size > 0) swatches[0].rgb else color
}