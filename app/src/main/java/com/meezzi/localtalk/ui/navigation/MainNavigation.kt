package com.meezzi.localtalk.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.addPost.AddPostScreen
import com.meezzi.localtalk.ui.addPost.AddPostViewModel
import com.meezzi.localtalk.ui.board.BoardScreen
import com.meezzi.localtalk.ui.chat.ChatScreen
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.home.screens.AddPostFloatingButton
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
            val homeRepository = HomeRepository(LocalContext.current)
            val homeViewModel = HomeViewModel(homeRepository)
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

        composable(Screen.Profile.route) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(Screens.CreateProfile.name)
                },
                profileViewModel = ProfileViewModel(UserRepository())
            )
        }

        composable(Screens.AddPost.name) {
            AddPostScreen(
                addPostViewModel = AddPostViewModel(),
                onNavigationBack =  {},
                onSavePost = {}
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigationView() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != Screens.AddPost.name) {
                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            if (currentDestination?.route == Screen.Home.route) {
                AddPostFloatingButton {
                    navController.navigate(Screens.AddPost.name)
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding()) {
            MainNavHost(navController = navController)
        }
    }
}