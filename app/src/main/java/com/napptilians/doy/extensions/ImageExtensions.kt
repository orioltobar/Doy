package com.napptilians.doy.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

private const val UTF_8 = "UTF-8"
private const val BASE_64_ENCODED_BITMAP_PREFIX = "data:image/png;base64,"
private val COMPRESS_FORMAT = Bitmap.CompressFormat.PNG
private const val COMPRESS_QUALITY = 30
private const val DEFAULT_IMAGE_WIDTH = 1536
private const val DEFAULT_IMAGE_HEIGHT = 1152

fun Uri.toByteArray(): ByteArray? = BitmapFactory.decodeFile(encodedPath)?.toByteArray()

fun Bitmap.toByteArray(): ByteArray? {
    val outputStream = ByteArrayOutputStream()
    compress(COMPRESS_FORMAT, COMPRESS_QUALITY, outputStream)
    return outputStream.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)

fun ByteArray.encodeByteArrayToBase64(): String? =
    if (isEmpty()) null
    else BASE_64_ENCODED_BITMAP_PREFIX + Base64.encodeToString(this, Base64.DEFAULT)

fun String.decodeByteArrayFromBase64(): ByteArray? {
    if (isBlank()) {
        return null
    }
    var urlDecodedBitmap: String
    try {
        // NOTE: Restoring plus signs as URLDecoder accidentally translates it into white spaces
        urlDecodedBitmap = URLDecoder.decode(this, UTF_8).replace(" ", "+")
        urlDecodedBitmap = urlDecodedBitmap.replace(BASE_64_ENCODED_BITMAP_PREFIX, "")
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        urlDecodedBitmap = this
    }
    return Base64.decode(urlDecodedBitmap, Base64.DEFAULT)
}

fun Bitmap.resize(
    maxWidth: Int = DEFAULT_IMAGE_WIDTH,
    maxHeight: Int = DEFAULT_IMAGE_HEIGHT
): Bitmap? = if (maxHeight > 0 && maxWidth > 0) {
    val ratioBitmap = width.toFloat() / height.toFloat()
    val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
    var finalWidth = maxWidth
    var finalHeight = maxHeight
    if (ratioMax > ratioBitmap) {
        finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
    } else {
        finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
    }
    Bitmap.createScaledBitmap(this, finalWidth, finalHeight, true)
} else {
    this
}
