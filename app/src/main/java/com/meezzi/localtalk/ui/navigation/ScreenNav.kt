package com.meezzi.localtalk.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.meezzi.localtalk.R

enum class Screens {
    Login,
    Home,
    CreateProfile,
    Profile,
}

sealed class Screen(
    val route: String,
    val title: @Composable () -> String,
    val selectedIcon: @Composable () -> Painter,
    val unselectedIcon: @Composable () -> Painter
) {
    data object Home : Screen(
        "home", { stringResource(R.string.home) },
        { painterResource(id = R.drawable.ic_home_filled) },
        { painterResource(id = R.drawable.ic_home_outlined) }
    )

    data object Board : Screen(
        "board", { stringResource(R.string.board) },
        { painterResource(id = R.drawable.ic_board_filled) },
        { painterResource(id = R.drawable.ic_board_outlined) }
    )

    data object Chat : Screen(
        "chat", { stringResource(R.string.chat) },
        { painterResource(id = R.drawable.ic_chat_filled) },
        { painterResource(id = R.drawable.ic_chat_outlined) }
    )

    data object Profile : Screen(
        "profile", { stringResource(R.string.profile) },
        { painterResource(id = R.drawable.ic_profile_filled) },
        { painterResource(id = R.drawable.ic_profile_outlined) }
    )
}