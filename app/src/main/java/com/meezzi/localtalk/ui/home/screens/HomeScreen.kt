package com.meezzi.localtalk.ui.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.ui.home.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    CheckPermissions {
        TopAppBar(homeViewModel)
    }

}

@Composable
fun TopAppBar(homeViewModel: HomeViewModel) {

    val address by homeViewModel.address.collectAsState()

    homeViewModel.getAddress()

    CustomTopAppBar(address)

}