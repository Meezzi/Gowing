package com.meezzi.localtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.meezzi.localtalk.repository.AuthRepository
import com.meezzi.localtalk.repository.PermissionRepository
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.common.SignInNavigation
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.navigation.MainNavigationView
import com.meezzi.localtalk.ui.profile.ProfileViewModel
import com.meezzi.localtalk.ui.theme.LocalTalkTheme

class MainActivity : ComponentActivity() {

    private val introViewModel by viewModels<IntroViewModel> {
        IntroViewModel.provideFactory(
            AuthRepository(this)
        )
    }

    private val profileViewModel by viewModels<ProfileViewModel> {
        ProfileViewModel.provideFactory(
            UserRepository()
        )
    }

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.provideFactory(
            PermissionRepository(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalTalkTheme {
                SignInNavigation(introViewModel, profileViewModel, homeViewModel)
                MainNavigationView()
            }
        }
    }
}