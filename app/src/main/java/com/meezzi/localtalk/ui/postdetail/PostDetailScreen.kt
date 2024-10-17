package com.meezzi.localtalk.ui.postdetail

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = post?.category?.name,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->

    }
}