package com.meezzi.localtalk.ui.boardDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.ui.common.NavigationTopAppBar

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