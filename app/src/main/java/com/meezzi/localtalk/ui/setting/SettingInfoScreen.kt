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