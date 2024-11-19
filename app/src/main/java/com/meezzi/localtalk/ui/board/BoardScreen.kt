package com.meezzi.localtalk.ui.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meezzi.localtalk.R

@Composable
fun BoardScreen(onNavigateToSearch: () -> Unit) {
    Scaffold(
        topBar = {
            BoardTopAppBar(
                title = stringResource(id = R.string.board),
                onNavigateToSearch = onNavigateToSearch
            )
        },
    ) { innerPadding ->
        BoardContentScreen(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardTopAppBar(
    title: String,
    onNavigateToSearch: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(title)
        },
        actions = {
            IconButton(onClick = { onNavigateToSearch() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "검색")
            }
        }
    )
}

@Composable
fun BoardContentScreen(innerPadding: PaddingValues) {

}

@Composable
fun BoardItem(
    icon: Painter? = null,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.titleMedium
        )
    }
}