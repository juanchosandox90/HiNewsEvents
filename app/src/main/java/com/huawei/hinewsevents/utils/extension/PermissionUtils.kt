package com.huawei.hinewsevents.utils.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat

open class PermissionUtils {

    companion object {
        private val TAG = PermissionUtils::class.java.simpleName

        val requestCodeLocation = 1003
        var permissionRequestsLocation = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val requestCodeBackgroundLocation = 1004
        var permissionRequestsBackgroundLocation =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )


        val requestCodeCameraAndGallery = 1005
        var permissionRequestsCameraAndGallery = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }



    // maybe we need this
    private fun checkSelfPermission(context: Context, whichPermission: Int): Boolean {
        Log.i(TAG,"checkSelfPermission whichPermission : $whichPermission")

        when (whichPermission) {

            PermissionUtils.requestCodeCameraAndGallery ->
                return ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED

            PermissionUtils.requestCodeLocation ->
                return ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED

            PermissionUtils.requestCodeBackgroundLocation ->
                return ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED

            else -> return false
        }

    }

}