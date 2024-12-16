package com.meezzi.localtalk.ui.setting

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.meezzi.localtalk.ui.common.CenterTopAppBar

@Composable
fun SettingInfoScreen(
    title: String,
    onNavigateToBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = title,
                onNavigateToBack = onNavigateToBack,
            )
        }
    ) { innerPadding ->

    }
}

@Composable
fun MyInformation(
    innerPadding: PaddingValues,
    email: String,
    onLogout: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        SettingsItem(
            title = email,
            subTitle = stringResource(R.string.logout),
            onClick = {
                showDialog = true
            }
        )

        if (showDialog) {
            CustomAlertDialog(
                message = stringResource(R.string.logout_confirmation_message),
                confirmButtonText = stringResource(id = R.string.yes),
                dismissButtonText = stringResource(id = R.string.no),
                onConfirm = {
                    showDialog = false
                    onLogout()
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}