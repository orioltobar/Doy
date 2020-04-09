package com.napptilians.doy.extensions

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.Fragment

/**
 * Launches a chooser [Intent] in order to allow the user to pick an image from the
 * filesystem or to take a picture from the camera.
 *
 * @param outputFileUri The output file [Uri]
 * @param requestCode The request code of the [android.app.Activity] Intent
 */
fun Fragment.launchSelectImageIntent(outputFileUri: Uri, requestCode: Int) {
    activity?.let {
        val chooserIntent = getSelectImageChooserIntent(it.packageManager, outputFileUri)
        if (chooserIntent.resolveActivity(it.packageManager) != null) {
            startActivityForResult(chooserIntent, requestCode)
        }
    }
}

private fun getSelectImageChooserIntent(packageManager: PackageManager, outputFileUri: Uri): Intent {
    // For camera
    val cameraIntents: MutableList<Intent> = ArrayList()
    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val listCam = packageManager.queryIntentActivities(captureIntent, 0)
    for (res in listCam) {
        val packageName = res.activityInfo.packageName
        val intent = Intent(captureIntent).apply {
            component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            setPackage(packageName)
            putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        }
        cameraIntents.add(intent)
    }

    // For image picker
    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    // Creating chooser
    return Intent.createChooser(galleryIntent, "").apply {
        putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray<Parcelable>())
    }
}
