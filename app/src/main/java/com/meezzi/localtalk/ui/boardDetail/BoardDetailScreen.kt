package com.meezzi.localtalk.ui.boardDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.ui.common.LoadingView
import com.meezzi.localtalk.ui.common.NavigationTopAppBar
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.home.screens.PostListSection
import com.meezzi.localtalk.ui.postdetail.ErrorView

@Composable
fun BoardDetailScreen(
    categoryId: String?,
    homeViewModel: HomeViewModel,
    boardDetailViewModel: BoardDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPostDetail: (String, String, String) -> Unit,
) {

    val categoryName = Categories.getDisplayNameById(categoryId?.uppercase() ?: "") ?: "로딩 중"

    val city by homeViewModel.address.collectAsState()
    val postList by boardDetailViewModel.postList.collectAsState()
    val isLoading by boardDetailViewModel.isLoading.collectAsState()
    val errorMessage by boardDetailViewModel.errorMessage.collectAsState()

    LaunchedEffect(city, categoryId) {
        if (categoryId != null) {
            boardDetailViewModel.fetchPostsByCategory(city.split(" ")[0], categoryId)
        }
    }

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = categoryName,
                onNavigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        BoardDetailContentScreen(
            innerPadding,
            isLoading,
            postList,
            errorMessage,
            onNavigateToPostDetail,
        )
    }
}

@Composable
fun BoardDetailContentScreen(
    innerPadding: PaddingValues,
    isLoading: Boolean,
    postList: List<Post>,
    errorMessage: String,
    onNavigateToPostDetail: (String, String, String) -> Unit
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        when {
            errorMessage.isNotEmpty() -> {
                ErrorView(message = errorMessage)
            }

            isLoading -> {
                LoadingView()
            }

            else -> {
                PostListView(
                    postList = postList,
                    onNavigateToPostDetail = onNavigateToPostDetail
                )
            }
        }
    }
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