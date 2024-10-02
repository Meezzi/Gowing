package com.meezzi.localtalk.ui.addPost

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R

@Composable
fun AddPostScreen(
    onNavigationBack: () -> Unit,
    onSavePost: () -> Unit
) {
    Scaffold(
        topBar = {
            AddPostTopAppBar(onNavigationBack, onSavePost)
        },
    ) { innerPadding ->
        Content(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPostTopAppBar(
    onNavigationBack: () -> Unit,
    onSavePost: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.write_post))
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back)
                )
            }
        },
        actions = {
            TextButton(onClick = { onSavePost() }) {
                Text(
                    text = stringResource(R.string.complete),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    )
}

@Composable
fun Content(innerPadding: PaddingValues) {

}