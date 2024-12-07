package com.meezzi.localtalk.ui.chat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
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