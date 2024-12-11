package com.meezzi.localtalk.ui.chat

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.Message
import com.meezzi.localtalk.ui.common.CustomPermissionRationaleDialog
import com.meezzi.localtalk.ui.common.NavigationMenuTopAppBar
import com.meezzi.localtalk.ui.common.ShowChatRoomExitDialog
import com.meezzi.localtalk.util.PermissionHandler
import com.meezzi.localtalk.util.TimeFormat
import kotlinx.coroutines.launch

@Composable
fun ChatRoomScreen(
    chatRoomId: String,
    chatViewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
) {
    var showPermissionRationaleDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userNickname by chatViewModel.userNickname.collectAsState()
    val profileImageUri by chatViewModel.profileImageUri.collectAsState()
    val chatContent by chatViewModel.chatContent.collectAsState()
    val currentUserId by chatViewModel.currentUserId.collectAsState()
    val messages by chatViewModel.messages.collectAsState()

    LaunchedEffect(chatRoomId) {
        chatViewModel.updateChatRoomStatus(chatRoomId)
    }

    LaunchedEffect(messages) {
        chatViewModel.fetchMessages(chatRoomId)
    }

    LaunchedEffect(userNickname, profileImageUri) {
        chatViewModel.fetchOtherUserNickname(chatRoomId)
        chatViewModel.fetchProfileImageUri(chatRoomId)
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val contentResolver = context.contentResolver
            uris.forEach { uri ->
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            chatViewModel.sendImages(chatRoomId, uris)
        }
    }

    val mediaPermission =
        rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
            if (isGranted) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                showPermissionRationaleDialog = true
            }
        }

    if (showDialog) {
        ShowChatRoomExitDialog(
            onConfirm = {
                showDialog = false
                chatViewModel.exitChatRoom(
                    chatRoomId = chatRoomId,
                    onSuccess = { onNavigateBack() },
                )
            },
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        topBar = {
            NavigationMenuTopAppBar(
                title = userNickname,
                menuItems = listOf(stringResource(id = R.string.chat_room_exit)),
                onMenuItemClick = {
                    showDialog = true
                },
                onNavigateBack = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        ChatRoomContentScreen(
            innerPadding,
            currentUserId = currentUserId,
            userNickname = userNickname,
            userProfileImage = profileImageUri,
            messages = messages,
            messageInput = chatContent,
            onContentChange = { chatViewModel.updateChatContent(it) },
            onSendMessage = {
                chatViewModel.sendMessage(
                    chatRoomId = chatRoomId,
                    messageContent = it
                )
            },
            onAddImageClick = {
                mediaPermission.launch(PermissionHandler.getMediaPermission())
            }
        )

        if (showPermissionRationaleDialog) {
            CustomPermissionRationaleDialog(
                text = stringResource(R.string.permission_dialog_media_content),
                onDismiss = { showPermissionRationaleDialog = false },
                onConfirm = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ChatRoomContentScreen(
    innerPadding: PaddingValues,
    currentUserId: String,
    userNickname: String,
    userProfileImage: Uri?,
    messages: List<Message>,
    messageInput: String,
    onContentChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onAddImageClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.scrollToItem(messages.lastIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            items(messages) { message ->
                MessageItem(userNickname, userProfileImage, message, currentUserId)
            }
        }

        MessageInputBar(messageInput, onContentChange, onSendMessage, onAddImageClick)
    }
}

@Composable
private fun MessageInputBar(
    messageInput: String,
    onContentChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onAddImageClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        IconButton(onClick = { onAddImageClick() }) {
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

@Composable
fun MessageItem(
    userNickname: String,
    userProfileImage: Uri?,
    message: Message,
    currentUserId: String,
) {
    val isCurrentUser = message.senderId == currentUserId

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp, bottom = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = if (isCurrentUser) Alignment.Bottom else Alignment.Top,
    ) {
        if (isCurrentUser) {
            Time(message)
            MessageContent(message, isCurrentUser, userNickname)
        } else {
            UserProfileImage(userProfileImage)
            Spacer(modifier = Modifier.width(8.dp))
            MessageContent(message, isCurrentUser, userNickname)
            Box(modifier = Modifier.align(Alignment.Bottom)) {
                Time(message)
            }
        }
    }
}

@Composable
fun UserProfileImage(userProfileImage: Uri?) {
    AsyncImage(
        model = userProfileImage ?: R.drawable.ic_user,
        contentDescription = stringResource(id = R.string.profile_image),
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MessageContent(
    message: Message,
    isCurrentUser: Boolean,
    userNickname: String,
) {
    Column {
        if (!isCurrentUser) {
            Text(
                text = userNickname,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 5.dp, end = 5.dp)
            )
        }

        if (message.type == "image" && message.imageUrl.isNotEmpty()) {
            message.imageUrl.forEach { url ->
                AsyncImage(
                    model = url,
                    contentDescription = stringResource(id = R.string.image),
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(top = 4.dp, bottom = 4.dp),
                    placeholder = painterResource(id = R.drawable.ic_default_image),
                    error = painterResource(id = R.drawable.ic_error_image),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCurrentUser) Color.Magenta.copy(alpha = 0.3f) else Color.LightGray,
                        shape = if (isCurrentUser)
                            RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)
                        else
                            RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
                    )
                    .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = message.content,
                )
            }
        }
    }
}

@Composable
private fun Time(message: Message) {
    val time = TimeFormat().formatMessageTime(message.timestamp)

    Text(
        text = time,
        color = Color.Gray,
        modifier = Modifier.padding(start = 7.dp, end = 7.dp),
        style = MaterialTheme.typography.bodySmall
    )
}