package com.meezzi.localtalk.ui.postdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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