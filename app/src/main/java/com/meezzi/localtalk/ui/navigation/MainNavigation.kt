package com.meezzi.localtalk.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.meezzi.localtalk.ui.boardDetail.BoardDetailScreen
import com.meezzi.localtalk.ui.addPost.AddPostScreen
import com.meezzi.localtalk.ui.addPost.AddPostViewModel
import com.meezzi.localtalk.ui.board.BoardScreen
import com.meezzi.localtalk.ui.boardDetail.BoardDetailViewModel
import com.meezzi.localtalk.ui.chat.ChatRoomScreen
import com.meezzi.localtalk.ui.chat.ChatScreen
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.ui.home.screens.AddPostFloatingButton
import com.meezzi.localtalk.ui.home.screens.HomeScreen
import com.meezzi.localtalk.ui.intro.IntroViewModel
import com.meezzi.localtalk.ui.intro.screens.LoginScreen
import com.meezzi.localtalk.ui.postdetail.ImageViewerScreen
import com.meezzi.localtalk.ui.postdetail.PostDetailScreen
import com.meezzi.localtalk.ui.postdetail.PostDetailViewModel
import com.meezzi.localtalk.ui.profile.CreateProfileScreen
import com.meezzi.localtalk.ui.profile.ProfileScreen
import com.meezzi.localtalk.ui.search.SearchScreen
import com.meezzi.localtalk.ui.setting.SettingInfoScreen
import com.meezzi.localtalk.ui.setting.SettingScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    addPostViewModel: AddPostViewModel,
    postDetailViewModel: PostDetailViewModel,
    boardDetailViewModel: BoardDetailViewModel,
) {

    val introViewModel: IntroViewModel = hiltViewModel()

    val startDestination = remember {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Screen.Home.route
        } else {
            Screens.Login.name
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screens.Login.name) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(Screens.CreateProfile.name) {
            CreateProfileScreen(
                onProfileSaved = { nickname, profileImage ->
                    profileViewModel.saveUserProfile(nickname, profileImage)
                    navController.navigate(Screen.Home.route)
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onNavigateToPostDetail = { city, categoryId, postId ->
                    navController.navigate("${Screens.PostDetail.name}/$city/$categoryId/$postId")
                }
            )
        }

        composable(Screen.Board.route) {
            BoardScreen(
                onNavigateToSearch = {
                    navController.navigate(Screens.Search.name)
                },
                onNavigateToMyPosts = {
                    navController.navigate("${Screens.BoardDetail.name}/my_posts")
                },
                onNavigateToMyComments = {
                    navController.navigate("${Screens.BoardDetail.name}/my_comments")
                },
                onNavigateToPostItem = { categoryId ->
                    val category = categoryId.lowercase()
                    navController.navigate("${Screens.BoardDetail.name}/$category")
                }
            )
        }

        composable(Screen.Chat.route) {
            ChatScreen(
                onChatRoomClick = { chatRoomId ->
                    navController.navigate("${Screens.ChatRoom.name}/$chatRoomId")
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onEditProfileClick = {
                    navController.navigate(Screens.CreateProfile.name)
                },
                onNavigateToPostDetail = { city, categoryId, postId ->
                    navController.navigate("${Screens.PostDetail.name}/$city/$categoryId/$postId")
                },
                onNavigateToSetting = {
                    navController.navigate(Screens.Setting.name)
                },
            )
        }

        composable(Screens.AddPost.name) {
            AddPostScreen(
                addPostViewModel = addPostViewModel,
                onNavigationBack = { navController.popBackStack() },
                onSavePost = { city, categoryId, postId ->
                    navController.popBackStack()
                    navController.navigate("${Screens.PostDetail.name}/$city/$categoryId/$postId")
                }
            )
        }

        composable(
            route = "${Screens.PostDetail.name}/{city}/{categoryId}/{postId}",
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType },
                navArgument("city") { type = NavType.StringType },
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: ""
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            PostDetailScreen(
                postId = postId,
                city = city,
                categoryId = categoryId,
                postDetailViewModel = postDetailViewModel,
                onNavigateBack = { navController.popBackStack() },
                onImageClick = { selectedImageIndex ->
                    postDetailViewModel.updateSelectedImageIndex(selectedImageIndex)
                    postDetailViewModel.updateImageList(
                        postDetailViewModel.post.value?.postImageUrl ?: emptyList()
                    )
                    navController.navigate(Screens.ImageViewer.name)
                },
                onNavigateChat = { chatRoomId ->
                    navController.navigate("${Screens.ChatRoom.name}/$chatRoomId")
                }
            )
        }

        composable(Screens.ImageViewer.name) {
            ImageViewerScreen(
                postDetailViewModel = postDetailViewModel,
                onDismiss = { navController.popBackStack() },
            )
        }

        composable(Screens.Search.name) {
            SearchScreen()
        }

        composable("${Screens.BoardDetail.name}/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            BoardDetailScreen(
                categoryId = categoryId,
                homeViewModel = homeViewModel,
                boardDetailViewModel = boardDetailViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPostDetail = { city, categoryId, postId ->
                    navController.navigate("${Screens.PostDetail.name}/$city/$categoryId/$postId")
                }
            )
        }

        composable("${Screens.ChatRoom.name}/{chatRoomId}") { backStackEntry ->
            val chatRoomId = backStackEntry.arguments?.getString("chatRoomId")
            ChatRoomScreen(
                chatRoomId = chatRoomId!!,
                onNavigateBack = { navController.popBackStack() })
        }

        composable(Screens.Setting.name) {
            SettingScreen(
                onNavigateToBack = { navController.popBackStack() },
                onNavigateToInfo = { title ->
                    navController.navigate("${Screens.SettingInfo.name}/$title")
                },
            )
        }

        composable("${Screens.SettingInfo.name}/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            SettingInfoScreen(
                title = title ?: "",
                onLogout = { introViewModel.signOutWithGoogle() },
                onNavigateToBack = { navController.popBackStack() },
                onNavigateToLogin = {
                    navController.navigate(Screens.Login.name) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenView(
    homeViewModel: HomeViewModel,
    addPostViewModel: AddPostViewModel,
    postDetailViewModel: PostDetailViewModel,
    boardDetailViewModel: BoardDetailViewModel,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route == Screen.Home.route ||
                currentDestination?.route == Screen.Board.route ||
                currentDestination?.route == Screen.Chat.route ||
                currentDestination?.route == Screen.Profile.route ||
                currentDestination?.route == Screens.PostDetail.name
            ) {
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
        Box {
            MainNavHost(
                navController = navController,
                homeViewModel = homeViewModel,
                addPostViewModel = addPostViewModel,
                postDetailViewModel = postDetailViewModel,
                boardDetailViewModel = boardDetailViewModel,
            )
        }
    }
}