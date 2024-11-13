package com.meezzi.localtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.meezzi.localtalk.repository.AuthRepository
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.PostSaveRepository
import com.meezzi.localtalk.repository.UserRepository
import com.meezzi.localtalk.ui.addPost.AddPostViewModel
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.navigation.MainScreenView
import com.meezzi.localtalk.ui.postdetail.PostDetailViewModel
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
            HomeRepository(this),
            PostSaveRepository()
        )
    }

    private val addPostViewModel by viewModels<AddPostViewModel> {
        AddPostViewModel.provideFactory(
            PostSaveRepository(),
            homeViewModel,
        )
    }

    private val postDetailViewModel by viewModels<PostDetailViewModel> {
        PostDetailViewModel.provideFactory(
            PostSaveRepository(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalTalkTheme {
                MainScreenView(
                    introViewModel = introViewModel,
                    profileViewModel = profileViewModel,
                    homeViewModel = homeViewModel,
                    addPostViewModel = addPostViewModel,
                    postDetailViewModel = postDetailViewModel,
                )
            }
        }
    }
}