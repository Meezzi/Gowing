package com.meezzi.localtalk.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.meezzi.localtalk.ui.home.screens.HomeScreen
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.intro.screens.LoginScreen

@Composable
fun SignInNavigation(introViewModel: IntroViewModel) {
    val navController = rememberNavController()

    val user by introViewModel.authState.collectAsStateWithLifecycle()

    val startDestination = if (user == null) Screens.Login.name else
        Screens.Home.name

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

        composable(Screens.Home.name) {
            HomeScreen(
                currentUser = FirebaseAuth.getInstance().currentUser,
                onSignOutClick = {
                    introViewModel.signOutWithGoogle()

                    navController.popBackStack()
                    navController.navigate(Screens.Login.name)
                })
        }
    }
}