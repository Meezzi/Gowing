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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.meezzi.localtalk.R

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditProfileClick: () -> Unit,
) {

    val nickname by profileViewModel.nickname.collectAsState()
    val region by profileViewModel.region.collectAsState()
    val profileImageUri by profileViewModel.profileImageUri.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.padding(25.dp))

        ProfileHeader(
            nickname,
            region,
            profileImageUri,
            onEditProfileClick = onEditProfileClick
        )

        Spacer(modifier = Modifier.padding(10.dp))

        PostOptionRow()
    }
}

@Composable
fun PostOptionRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomButton(text = stringResource(R.string.written_post), onClick = { })
        CustomButton(text = stringResource(R.string.recommended_post), onClick = { })
    }
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        border = BorderStroke(0.5.dp, Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.Black,
            containerColor = Color.Transparent,
        ),
        shape = RectangleShape,
    ) {
        Text(text)
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

            val painter = if (profileImage != null) {
                rememberAsyncImagePainter(model = profileImage)
            } else {
                painterResource(id = R.drawable.ic_default_profile)
            }

            Image(
                painter = painter,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = "Region",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                )
            }
        }
        IconButton(
            onClick = { onEditProfileClick() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_note_pencil),
                contentDescription = "Favorite",
                Modifier.size(30.dp),
            )
        }
    }
}