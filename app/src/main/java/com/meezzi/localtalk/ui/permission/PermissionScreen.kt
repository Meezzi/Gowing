package com.meezzi.localtalk.ui.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.CustomTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    if (permissionState.allPermissionsGranted) {
        onNavigateToHome()
    } else {
        Scaffold(
            topBar = {
                CustomTopAppBar(title = stringResource(R.string.permission_header_title))
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { openAppSettings(context) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(R.string.setting)
                    )
                }
            }
        ) { innerPadding ->
            PermissionScreenContent(
                innerPadding = innerPadding,
                state = permissionState,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionScreenContent(
    innerPadding: PaddingValues,
    state: MultiplePermissionsState,
) {
    var showRationale by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        PermissionContent()

        Spacer(modifier = Modifier.height(20.dp))

        ConfirmButton(state) { showRationale = true }
    }

    if (showRationale) {
        PermissionRationaleDialog(
            state = state,
            onDismiss = { showRationale = false }
        )
    }
}

@Composable
private fun PermissionContent() {
    Text(
        text = stringResource(R.string.permission_location_title),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Row(verticalAlignment = Alignment.Top) {
        Image(
            modifier = Modifier.padding(5.dp),
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = stringResource(R.string.permission_location),
        )

        Column(modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(R.string.location),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                text = stringResource(R.string.permission_location_content),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ConfirmButton(
    state: MultiplePermissionsState,
    onShowRationale: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        onClick = {
            if (state.shouldShowRationale) {
                onShowRationale()
            } else {
                state.launchMultiplePermissionRequest()
            }
        }
    ) {
        Text(text = stringResource(R.string.confirm))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionRationaleDialog(
    state: MultiplePermissionsState,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.permission_dialog_title)) },
        text = { Text(text = stringResource(R.string.permission_dialog_content)) },
        confirmButton = {
            TextButton(onClick = {
                state.launchMultiplePermissionRequest()
                onDismiss()
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

private fun openAppSettings(context: android.content.Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}