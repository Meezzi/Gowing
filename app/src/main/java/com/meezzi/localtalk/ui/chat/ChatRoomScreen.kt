package com.meezzi.localtalk.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.ui.common.NavigationMenuTopAppBar

@Composable
fun ChatRoomScreen(
    chatViewModel: ChatViewModel,
) {

    val userNickname by chatViewModel.userNickname.collectAsState()

    Scaffold(
        topBar = {
            NavigationMenuTopAppBar(
                title = userNickname,
                menuItems = listOf(stringResource(id = R.string.chat_room_exit)),
                onMenuItemClick = { },
                onNavigateBack = { },
            )
        },
    ) { innerPadding ->
        ChatRoomContentScreen(
            innerPadding,
        )
    }
}

@Composable
fun ChatRoomContentScreen(
    innerPadding: PaddingValues,
) {

}

@Composable
private fun MessageInputBar(
    messageInput: String,
    onContentChange: (String) -> Unit,
    onSendMessage: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.LightGray,
            )
        }

        BasicTextField(
            value = messageInput,
            onValueChange = onContentChange,
            modifier = Modifier.weight(1f),
            maxLines = 5,
            decorationBox = { innerTextField ->
                Box {
                    if (messageInput.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.chat_room_send_message),
                            color = Color.Black.copy(alpha = 0.3f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }
            }
        )
        ChatSubmitButton(messageInput, onSendMessage)
    }
}

@Composable
private fun ChatSubmitButton(
    chatContent: String,
    onChatSubmit: (String) -> Unit,
) {
    IconButton(
        onClick = {
            if (chatContent.isNotEmpty()) {
                onChatSubmit(chatContent)
            }
        },
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = stringResource(id = R.string.chat_room_send_message),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}