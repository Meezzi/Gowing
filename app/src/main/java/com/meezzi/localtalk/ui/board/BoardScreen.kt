package com.meezzi.localtalk.ui.board

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.CustomSearchTopAppBar

@Composable
fun BoardScreen(onNavigateToSearch: () -> Unit) {
    Scaffold(
        topBar = {
            CustomSearchTopAppBar(
                title = stringResource(id = R.string.board),
                onNavigateToSearch = onNavigateToSearch
            )
        },
    ) { innerPadding ->
        BoardContentScreen(innerPadding)
    }
}

@Composable
fun BoardContentScreen(innerPadding: PaddingValues) {

}