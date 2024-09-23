package com.meezzi.localtalk.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meezzi.localtalk.repository.PermissionRepository
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.board.BoardScreen
import com.meezzi.localtalk.ui.chat.ChatScreen
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.home.screens.HomeScreen
import com.meezzi.localtalk.ui.profile.ProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileViewModel

@Composable
fun MainNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {

        composable(Screen.Home.route) {
            val permissionRepository = PermissionRepository(LocalContext.current)
            val homeViewModel = HomeViewModel(permissionRepository)
            HomeScreen(
                homeViewModel = homeViewModel
            )
        }

        composable(Screen.Board.route) {
            BoardScreen()
        }

        composable(Screen.Chat.route) {
            ChatScreen()
        }

        composable(Screens.Profile.name) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(Screens.CreateProfile.name)
                },
                profileViewModel = ProfileViewModel(UserRepository())
            )
        }
    }
}

@Composable
fun MainNavigationView() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Box(modifier = Modifier.padding(it)) {
            MainNavHost(navController = navController)
        }
    }
}