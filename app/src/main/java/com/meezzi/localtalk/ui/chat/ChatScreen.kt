package com.meezzi.localtalk.ui.chat

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.ChatRoom
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.util.TimeFormat

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(title = stringResource(id = R.string.chat))
        },
    ) { innerPadding ->
    }
}

@Composable
fun ChatRoomList(
    innerPadding: PaddingValues,
    chatRoomInfo: List<Pair<ChatRoom, Pair<String, Uri?>>>,
) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(chatRoomInfo) { (chatRoom, userInfo) ->
            val (nickname, profileImageUri) = userInfo
            ChatRoomItem(
                chatRoom = chatRoom,
                nickname = nickname,
                profileImageUri = profileImageUri
            )
        }
    }
}

@Composable
fun ChatRoomItem(chatRoom: ChatRoom, nickname: String, profileImageUri: Uri?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatRoomImage(profileImageUri)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nickname,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = chatRoom.lastMessage,
                color = Color.DarkGray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = TimeFormat().formatMessageTime(chatRoom.lastMessageTime!!),
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ChatRoomImage(image: Uri?) {
    AsyncImage(
        model = image ?: R.drawable.ic_user,
        contentDescription = stringResource(id = R.string.profile_image),
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}