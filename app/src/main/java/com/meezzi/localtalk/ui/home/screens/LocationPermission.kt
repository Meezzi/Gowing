package com.meezzi.localtalk.ui.home.screens

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.meezzi.localtalk.ui.permission.PermissionScreen

@Composable
fun CheckPermissions(onPermissionsGranted: @Composable () -> Unit) {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            onPermissionsGranted()
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionBox(
    permissions: List<String>,
    requiredPermissions: List<String> = permissions,
    onGranted: @Composable () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    val allRequiredPermissionsGranted =
        permissionState.revokedPermissions.none { it.permission in requiredPermissions }

    Box {
        if (allRequiredPermissionsGranted) {
            onGranted()
        } else {
            PermissionScreen(
                state = permissionState,
            )
        }
    }
}