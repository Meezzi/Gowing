package com.meezzi.localtalk.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val screens = listOf(
        Screen.Home,
        Screen.Board,
        Screen.Chat,
        Screen.Profile,
    )

    NavigationBar(
        modifier = Modifier.height(115.dp),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = if (currentRoute == item.route) item.selectedIcon() else item.unselectedIcon(),
                        contentDescription = item.title.toString(),
                        modifier = Modifier.size(23.dp),
                    )
                },
                label = {
                    Text(
                        text = item.title(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}