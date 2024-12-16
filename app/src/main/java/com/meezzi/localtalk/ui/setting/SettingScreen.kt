package com.meezzi.localtalk.ui.setting

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.CenterTopAppBar

@Composable
fun SettingScreen(
    onNavigateToBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterTopAppBar(
                title = stringResource(id = R.string.setting),
                onNavigateToBack = onNavigateToBack
            )
        },
    ) { innerPadding ->
        SettingContentScreen(innerPadding)
    }
}

@Composable
fun SettingContentScreen(innerPaddingValues: PaddingValues) {

}