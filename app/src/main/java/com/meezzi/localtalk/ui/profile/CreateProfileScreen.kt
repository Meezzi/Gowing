package com.meezzi.localtalk.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.TextTitleLarge

@Composable
fun CreateProfileScreen(
    onNavigateBack: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    var profileImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    var nickname by rememberSaveable { mutableStateOf("") }
    val isNicknameValid by profileViewModel.isNicknameValid.collectAsState()
    val nicknameErrorMessage by profileViewModel.nicknameErrorMessage.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            TextTitleLarge("프로필을 설정해주세요")

            Spacer(modifier = Modifier.height(40.dp))

            ProfileImage(
                profileImageUri = profileImageUri,
                onEditProfileImage = {
                    galleryLauncher.launch("image/*")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            EditNicknameField(
                nickname = nickname,
                onNicknameChange = { newNickname -> nickname = newNickname },
                nicknameErrorMessage = nicknameErrorMessage,
                profileViewModel = profileViewModel,
            )

            Spacer(modifier = Modifier.height(10.dp))

        }
        SaveProfileButton(nickname, profileImageUri, isNicknameValid) {
            profileViewModel.saveUserProfile(nickname, profileImageUri)
            onNavigateBack()
        }

    }
}

@Composable
private fun ProfileImage(
    profileImageUri: Uri?,
    onEditProfileImage: () -> Unit
) {

    val painter = if (profileImageUri != null) {
        rememberAsyncImagePainter(model = profileImageUri)
    } else {
        painterResource(id = R.drawable.ic_default_profile)
    }

    Image(
        painter = painter,
        contentDescription = "Profile Image",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable { onEditProfileImage() },
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun SaveProfileButton(
    nickname: String,
    profileImage: Uri?,
    isNicknameValid: Boolean,
    onSave: () -> Unit,
) {
    Button(
        onClick = onSave,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape,
        enabled = isNicknameValid,
    ) {
        TextTitleLarge(stringResource(R.string.save))
    }
}

@Composable
fun EditNicknameField(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    nicknameErrorMessage: String?,
    profileViewModel: ProfileViewModel,
) {
    OutlinedTextField(
        value = nickname,
        onValueChange = { text ->
            onNicknameChange(text)
            profileViewModel.onNicknameChange(text)
        },
        label = { Text(stringResource(R.string.nickname)) },
        isError = nicknameErrorMessage != null,
        supportingText = {
            if (nicknameErrorMessage != null) {
                Text(
                    text = nicknameErrorMessage,
                    color = Color.Red
                )
            }
        },
        placeholder = { Text(stringResource(R.string.nickname)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    )
}