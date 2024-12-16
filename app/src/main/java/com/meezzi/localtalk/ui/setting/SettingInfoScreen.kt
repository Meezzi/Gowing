package com.meezzi.localtalk.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.CenterTopAppBar
import com.meezzi.localtalk.ui.common.CustomAlertDialog
import com.meezzi.localtalk.ui.profile.ProfileViewModel

@Composable
fun SettingInfoScreen(
    title: String,
    profileViewModel: ProfileViewModel,
    onNavigateToBack: () -> Unit,
) {

    val email by profileViewModel.email.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.fetchUserEmail()
    }

    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = title,
                onNavigateToBack = onNavigateToBack,
            )
        }
    ) { innerPadding ->
        when (title) {
            stringResource(id = R.string.my_information) -> {
                MyInformation(innerPadding, email)
            }
        }
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