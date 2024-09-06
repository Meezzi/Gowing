package com.meezzi.localtalk.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.home.screens.HomeScreen
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.intro.screens.LoginScreen
import com.meezzi.localtalk.ui.profile.CreateProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileViewModel

@Composable
fun SignInNavigation(introViewModel: IntroViewModel, profileViewModel: ProfileViewModel) {

    val navController = rememberNavController()

    val user by introViewModel.authState.collectAsStateWithLifecycle()

    val startDestination = remember {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Screens.Home.name
        } else {
            Screens.Login.name
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            val hasData = introViewModel.hasUserData()
            val destination = if (hasData) Screens.Home.name else Screens.CreateProfile.name
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
                onProfileSaved = { nickname ->
                    profileViewModel.saveUserProfile(nickname)
                    navController.popBackStack()
                },
                profileViewModel = profileViewModel,
            )
        }

        composable(Screens.Home.name) {
            HomeScreen(
                currentUser = FirebaseAuth.getInstance().currentUser,
                onSignOutClick = {
                    introViewModel.signOutWithGoogle()

                    navController.popBackStack()
                    navController.navigate(Screens.Login.name)
                })
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