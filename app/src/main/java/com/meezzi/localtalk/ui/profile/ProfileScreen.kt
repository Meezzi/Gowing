package com.meezzi.localtalk.ui.profile

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.ui.common.LoadingView

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit,
) {
    val isLoading by profileViewModel.isLoading.collectAsState()
    val nickname by profileViewModel.nickname.collectAsState()
    val region by profileViewModel.region.collectAsState()
    val profileImageUri by profileViewModel.profileImageUri.collectAsState()

    if (isLoading) {
        LoadingView()
    } else {
        Scaffold(
            topBar = { CustomTopAppBar(title = "프로필") }
        ) { innerPadding ->
            ProfileContentScreen(
                innerPadding,
                nickname,
                region,
                profileImageUri,
                onEditProfileClick,
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
    onEditProfileClick: () -> Unit,
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