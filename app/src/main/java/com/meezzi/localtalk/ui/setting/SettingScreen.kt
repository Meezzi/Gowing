package com.meezzi.localtalk.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
fun SettingContentScreen(
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        SettingsItem(
            title = stringResource(R.string.my_information),
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    subTitle: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        if (subTitle != null) {
            Text(
                text = subTitle,
                color = Color.Gray,
                style = MaterialTheme.typography.titleMedium
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_next_arrow),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}