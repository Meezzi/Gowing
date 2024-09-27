package com.meezzi.localtalk.ui.home.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.meezzi.localtalk.ui.home.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    CheckPermissions()

}

@Composable
fun HomeContent() {
    Text(text = "HomeScreen")
}