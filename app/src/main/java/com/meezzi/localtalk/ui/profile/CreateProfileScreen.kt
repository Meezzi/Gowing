package com.meezzi.localtalk.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.TextBodyMedium
import com.meezzi.localtalk.ui.common.TextTitleLarge

@Composable
fun CreateProfileScreen(onProfileSaved: (String) -> Unit) {

    var nickname by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            TextTitleLarge(stringResource(R.string.nickname_input))

            Spacer(modifier = Modifier.height(50.dp))

            EditNicknameField(
                nickname = nickname,
                onNicknameChange = { newNickname -> nickname = newNickname },
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextBodyMedium(
                text = stringResource(R.string.nickname_input_hint),
                color = Color.DarkGray,
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
        SaveProfileButton(nickname, onProfileSaved)

    }
}

@Composable
private fun SaveProfileButton(
    nickname: String,
    onProfileSaved: (String,) -> Unit
) {
    Button(
        onClick = { onProfileSaved(nickname) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RectangleShape,
    ) {
        TextTitleLarge(stringResource(R.string.save))
    }
}


@Composable
fun EditNicknameField(
    nickname: String,
    onNicknameChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = nickname,
        onValueChange = onNicknameChange,
        label = { Text(stringResource(R.string.nickname)) },
        placeholder = { Text(stringResource(R.string.nickname)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
    )
}