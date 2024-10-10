package com.meezzi.localtalk.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.addPost.AddPostScreen
import com.meezzi.localtalk.ui.addPost.AddPostViewModel
import com.meezzi.localtalk.ui.board.BoardScreen
import com.meezzi.localtalk.ui.chat.ChatScreen
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.home.screens.AddPostFloatingButton
import com.meezzi.localtalk.ui.home.screens.HomeScreen
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.intro.screens.LoginScreen
import com.meezzi.localtalk.ui.profile.CreateProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    introViewModel: IntroViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    addPostViewModel: AddPostViewModel,
) {

    val user by introViewModel.authState.collectAsStateWithLifecycle()

    val startDestination = remember {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Screen.Home.route
        } else {
            Screens.Login.name
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            val hasData = introViewModel.hasUserData()
            val destination = if (hasData) Screen.Home.route else Screens.CreateProfile.name
            navController.navigate(destination)
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screens.Login.name) {
            LoginScreen(
                onSignInClick = {
                    introViewModel.signInWithGoogle()
                },
            )
        }

        composable(Screens.CreateProfile.name) {
            CreateProfileScreen(
                onProfileSaved = { nickname, profileImage ->
                    profileViewModel.saveUserProfile(nickname, profileImage)
                    navController.navigate(Screens.Profile.name)
                },
                profileViewModel = profileViewModel,
            )
        }

        composable(Screens.Profile.name) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(Screens.CreateProfile.name)
                },
                profileViewModel = ProfileViewModel(UserRepository())
            )
        }

        composable(Screen.Home.route) {
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
                addPostViewModel = addPostViewModel,
                onNavigationBack = { navController.popBackStack() },
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