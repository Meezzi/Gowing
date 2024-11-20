package com.meezzi.localtalk.ui.boardDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.ui.common.NavigationTopAppBar
import com.meezzi.localtalk.ui.home.screens.PostListSection

@Composable
fun BoardDetailScreen(
    categoryId: String?,
    onNavigateBack: () -> Unit,
) {

    val categoryName = Categories.getDisplayNameById(categoryId?.uppercase() ?: "") ?: "로딩 중"

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = categoryName,
                onNavigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        BoardDetailContentScreen(innerPadding)
    }
}

@Composable
fun BoardDetailContentScreen(innerPaddingValues: PaddingValues) {

}

@Composable
fun PostListView(
    postList: List<Post>,
    onNavigateToPostDetail: (String, String, String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PostListSection(
                postList = postList,
                onNavigateToPostDetail = onNavigateToPostDetail
            )
        }
    }
}