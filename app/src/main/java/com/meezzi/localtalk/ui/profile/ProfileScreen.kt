package com.meezzi.localtalk.ui.profile

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.data.ProfileTab
import com.meezzi.localtalk.ui.boardDetail.PostListView
import com.meezzi.localtalk.ui.common.LoadingView
import com.meezzi.localtalk.ui.common.NavigationMenuTopAppBar
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClick: () -> Unit,
    onNavigateToPostDetail: (String, String, String) -> Unit,
    onNavigateToSetting: () -> Unit,
) {
    val isLoading by profileViewModel.isLoading.collectAsState()
    val nickname by profileViewModel.nickname.collectAsState()
    val region by profileViewModel.region.collectAsState()
    val profileImageUri by profileViewModel.profileImageUri.collectAsState()
    val myPosts by profileViewModel.myPosts.collectAsState()
    val likedPosts by profileViewModel.likedPosts.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadRegion()
        profileViewModel.loadUserProfile()
    }

    LaunchedEffect(region) {
        if (region.isNotEmpty() && region != "지역 없음") {
            profileViewModel.loadMyPosts(region)
            profileViewModel.loadLikedPost()
        }
    }

    if (isLoading) {
        LoadingView()
    } else {
        Scaffold(
            topBar = {
                NavigationMenuTopAppBar(
                    title = stringResource(id = R.string.profile),
                    menuItems = listOf("설정"),
                    onMenuItemClick = {
                        onNavigateToSetting()
                    },
                )
            }
        ) { innerPadding ->
            ProfileContentScreen(
                innerPadding,
                nickname,
                region,
                profileImageUri,
                myPosts,
                likedPosts,
                onEditProfileClick,
                onNavigateToPostDetail,
            )
        }
    }
}

@Composable
fun ProfileContentScreen(
    innerPadding: PaddingValues,
    nickname: String,
    region: String,
    profileImageUri: Uri?,
    myPosts: List<Post>,
    likedPosts: List<Post>,
    onEditProfileClick: () -> Unit,
    onNavigateToPostDetail: (String, String, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        ProfileHeader(
            name = nickname,
            region = region,
            profileImage = profileImageUri,
            onEditProfileClick = onEditProfileClick,
        )
        ProfileTabs(myPosts, likedPosts, onNavigateToPostDetail)
    }
}

@Composable
fun ProfileHeader(
    name: String,
    region: String,
    profileImage: Uri?,
    onEditProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(profileImage)

            Spacer(modifier = Modifier.width(16.dp))

            ProfileNameAndRegion(name, region)
        }

        IconButton(
            onClick = { onEditProfileClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_note_pencil),
                contentDescription = stringResource(id = R.string.profile_edit),
                modifier = Modifier.size(30.dp),
            )
        }
    }
}

@Composable
private fun ProfileImage(profileImage: Uri?) {
    AsyncImage(
        model = profileImage ?: R.drawable.ic_default_profile,
        contentDescription = stringResource(
            id = R.string.profile_image
        ),
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun ProfileNameAndRegion(name: String, region: String) {
    Column {
        Text(
            text = name,
            fontSize = 25.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = region,
            fontSize = 16.sp,
            color = Color.DarkGray,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTabs(
    myPosts: List<Post>,
    likedPosts: List<Post>,
    onNavigateToPostDetail: (String, String, String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { ProfileTab.entries.size })

    Column {
        PrimaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            divider = { }
        ) {
            ProfileTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = tab.text, overflow = TextOverflow.Ellipsis) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (ProfileTab.entries[page]) {
                ProfileTab.MyPost -> PostListView(myPosts, onNavigateToPostDetail)
                ProfileTab.LikedPost -> PostListView(likedPosts, onNavigateToPostDetail)
            }
        }
    }
}