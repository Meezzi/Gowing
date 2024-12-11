package com.meezzi.localtalk.ui.chat

import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.ChatRoom
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.ui.common.EmptyView
import com.meezzi.localtalk.ui.common.LoadingView
import com.meezzi.localtalk.ui.common.ShowChatRoomExitDialog
import com.meezzi.localtalk.util.TimeFormat
import com.meezzi.localtalk.util.vibrate

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    onChatRoomClick: (String) -> Unit,
) {
    val context = LocalContext.current
    val isLoading by chatViewModel.isLoading.collectAsState()
    val chatRoomInfo by chatViewModel.chatRoomInfo.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedChatRoomId by remember { mutableStateOf("") }

    LaunchedEffect(chatRoomInfo) {
        chatViewModel.fetchChatRoomListWithDetails()
    }

    if (showDialog) {
        ShowChatRoomExitDialog(
            onConfirm = {
                showDialog = false
                chatViewModel.exitChatRoom(selectedChatRoomId) {
                    chatViewModel.fetchChatRoomListWithDetails()
                }
            },
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = stringResource(id = R.string.chat))
        },
    ) { innerPadding ->
        ChatContentScreen(innerPadding, isLoading, chatRoomInfo, onChatRoomClick)
        ChatContentScreen(
            innerPadding,
            isLoading,
            chatRoomInfo,
            onChatRoomClick,
            onChatRoomLongClick = { chatRoomId ->
                selectedChatRoomId = chatRoomId
                showDialog = true
            })
    }
}

@Composable
fun ChatContentScreen(
    innerPadding: PaddingValues,
    isLoading: Boolean,
    chatRoomInfo: List<Pair<ChatRoom, Pair<String, Uri?>>>,
    onChatRoomClick: (String) -> Unit,
    onChatRoomLongClick: (String) -> Unit,
) {
    when {
        isLoading -> LoadingView()
        chatRoomInfo.isEmpty() -> EmptyView(stringResource(id = R.string.no_chat_rooms))
        else -> ChatRoomList(
            innerPadding,
            chatRoomInfo,
            onChatRoomClick,
            onChatRoomLongClick
        )
    }
}

@Composable
fun ChatRoomList(
    innerPadding: PaddingValues,
    chatRoomInfo: List<Pair<ChatRoom, Pair<String, Uri?>>>,
    onChatRoomClick: (String) -> Unit,
    onChatRoomLongClick: (String) -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(chatRoomInfo) { (chatRoom, userInfo) ->
            val (nickname, profileImageUri) = userInfo
            ChatRoomItem(
                chatRoom = chatRoom,
                nickname = nickname,
                profileImageUri = profileImageUri,
                onChatRoomClick = { onChatRoomClick(chatRoom.chatRoomId) },
                onChatRoomLongClick = { onChatRoomLongClick(chatRoom.chatRoomId) }
            )
        }
    }
}

@Composable
fun ChatRoomItem(
    chatRoom: ChatRoom,
    nickname: String,
    profileImageUri: Uri?,
    onChatRoomClick: () -> Unit,
    onChatRoomLongClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onChatRoomLongClick()
                    },
                    onTap = { onChatRoomClick() }
                )
            },
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