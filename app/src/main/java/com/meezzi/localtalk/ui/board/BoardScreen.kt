package com.meezzi.localtalk.ui.board

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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