package com.meezzi.localtalk.util

import android.os.Build

class PermissionHandler {

    companion object {
        fun getMediaPermission(): String {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                    android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    android.Manifest.permission.READ_MEDIA_IMAGES
                }

                else -> android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }
    }
}