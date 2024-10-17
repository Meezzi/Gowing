package com.meezzi.localtalk.ui.postdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.NavigationTopAppBar

@Composable
fun PostDetailScreen(
    city: String,
    category: String,
    postId: String,
    postDetailViewModel: PostDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val post by postDetailViewModel.post.collectAsState()
    val errorMessage by postDetailViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = post?.category?.name,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                errorMessage != null -> {
                    ErrorView(
                        message = errorMessage ?: stringResource(R.string.error_message_not_found)
                    )
                }

                post == null -> {
                    LoadingView()
                }

                else -> {
                    PostContentView()
                }
            }
        }
    }
}

@Composable
fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
