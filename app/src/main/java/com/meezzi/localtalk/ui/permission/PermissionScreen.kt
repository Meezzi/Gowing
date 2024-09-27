package com.meezzi.localtalk.ui.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.meezzi.localtalk.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    state: MultiplePermissionsState,
) {
    val context = LocalContext.current

    var showRationale by remember(state) {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            PermissionHeader()

            Spacer(modifier = Modifier.padding(10.dp))

            PermissionContent()

            ConfirmButton(state) { showRationale = true }
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    data = Uri.parse("package:${context.packageName}")
                }
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = stringResource(R.string.setting)
            )
        }
    }

    if (showRationale) {
        PermissionRationaleDialog(state) { showRationale = false }
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

@Composable
private fun PermissionHeader() {
    Text(
        text = stringResource(R.string.permission_header_title),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
    )

    Spacer(modifier = Modifier.padding(5.dp))

    Text(
        text = stringResource(R.string.permission_header_content),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ConfirmButton(
    state: MultiplePermissionsState, onShowRationale: () -> Unit
) {
    Button(modifier = Modifier.fillMaxWidth(),
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
fun PermissionRationaleDialog(
    state: MultiplePermissionsState, onDismiss: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.permission_dialog_title))
        },
        text = {
            Text(text = stringResource(R.string.permission_dialog_content))
        },
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