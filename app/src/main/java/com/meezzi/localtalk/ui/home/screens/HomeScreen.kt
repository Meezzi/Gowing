package com.meezzi.localtalk.ui.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.meezzi.localtalk.permission.RequestLocationPermission
import com.meezzi.localtalk.ui.home.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    val locationPermissionGranted by homeViewModel.locationPermissionGranted.collectAsState()

    if (!locationPermissionGranted) {
        RequestLocationPermission()
    }
}