package com.meezzi.localtalk.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.meezzi.localtalk.R

@Composable
fun TextTitleLarge(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
) {
    TopAppBar(
        title = {
            Text(title)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(
    title: String?,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title ?: stringResource(id = R.string.action_loading))
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationMenuTopAppBar(
    title: String?,
    menuItems: List<String>,
    onMenuItemClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title ?: stringResource(id = R.string.action_loading)) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back)
                )
            }
        },
        actions = {
            MenuDropdownButton(
                menuItems = menuItems,
                onMenuItemClick = {
                    isMenuExpanded = false
                    onMenuItemClick(it)
                }
            )
        }
    )
}

@Composable
private fun MenuDropdownButton(
    menuItems: List<String>,
    onMenuItemClick: (String) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { isMenuExpanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.menu)
        )

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            menuItems.forEach { menuItem ->
                DropdownMenuItem(
                    text = { Text(menuItem) },
                    onClick = {
                        isMenuExpanded = false
                        onMenuItemClick(menuItem)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomPermissionRationaleDialog(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.confirm))
            }
        },
        title = { Text(stringResource(id = R.string.permission_dialog_title)) },
        text = { Text(text) },
    )
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyPostMessage() {
    Text(
        text = stringResource(R.string.home_empty_post),
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Gray,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CustomAlertDialog(
    message: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Text(
                text = message,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = Color.Magenta.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                Text(
                    text = confirmButtonText,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = dismissButtonText,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
    )
}

@Composable
fun EmptyView(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            color = Color.Gray
        )
    }
}

@Composable
fun ShowChatRoomExitDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    CustomAlertDialog(
        message = stringResource(id = R.string.chat_room_exit_message),
        confirmButtonText = stringResource(id = R.string.chat_room_exit_yes_message),
        dismissButtonText = stringResource(id = R.string.cancel),
        onConfirm = { onConfirm() },
        onDismiss = { onDismiss() }
    )
}