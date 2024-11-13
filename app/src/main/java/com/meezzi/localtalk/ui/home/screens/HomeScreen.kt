package com.meezzi.localtalk.ui.home.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.ui.home.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    CheckPermissions {
        HomeScreenContent(homeViewModel)
    }

}

@Composable
fun HomeScreenContent(homeViewModel: HomeViewModel) {

    val address by homeViewModel.address.collectAsState()

    homeViewModel.getAddress()

    Scaffold(
        topBar = {
            CustomTopAppBar(address)
        },
    ) { innerPadding ->
    }
}

}

@Composable
fun AddPostFloatingButton(
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        onClick = {
            onClick()
        },
        icon = {
            Icon(Icons.Filled.Add, "글 쓰기")
        },
        text = {
            Text(text = "글 쓰기")
        },
    )
}