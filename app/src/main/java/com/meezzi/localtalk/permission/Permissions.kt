package com.meezzi.localtalk.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.meezzi.localtalk.ui.common.CustomDialogLocation


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission() {
    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    if (!locationPermissionState.status.isGranted) {
        Column {
            CustomDialogLocation() {
                locationPermissionState.launchPermissionRequest()
                if (!locationPermissionState.status.isGranted) {
                    locationPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}