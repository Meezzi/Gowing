package com.meezzi.localtalk.ui.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.ui.common.CustomTopAppBar
import com.meezzi.localtalk.ui.common.EmptyPostMessage
import com.meezzi.localtalk.ui.common.LoadingView
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.util.TimeFormat

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    CheckPermissions {
        HomeScreenContent(homeViewModel)
    }

}

@Composable
fun HomeScreenContent(homeViewModel: HomeViewModel) {

    val address by homeViewModel.address.collectAsState()
    val hotPostList by homeViewModel.hotPostList.collectAsState()
    val latestPostList by homeViewModel.latestPostList.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()

    LaunchedEffect(hotPostList, latestPostList) {
        homeViewModel.getAddress()
        homeViewModel.getHotPostList()
        homeViewModel.getLatestPostList()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(address)
        },
    ) { innerPadding ->
        PostLists(innerPadding, hotPostList, latestPostList, isLoading)
    }
}

@Composable
fun PostLists(
    innerPadding: PaddingValues,
    hotPostList: List<Post>,
    latestPostList: List<Post>,
    isLoading: Boolean,
) {
    if (isLoading) {
        LoadingView()
    } else {
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PostListSection(
                    title = stringResource(id = R.string.home_hot_posts),
                    icon = painterResource(id = R.drawable.ic_fire),
                    postList = hotPostList
                )
            }

            item {
                PostListSection(
                    title = stringResource(id = R.string.home_latest_posts),
                    icon = painterResource(id = R.drawable.ic_light),
                    postList = latestPostList
                )
            }
        }
    }
}

@Composable
fun PostListSection(title: String, icon: Painter, postList: List<Post>) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = stringResource(id = R.string.icon),
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
        }

        if (postList.isNotEmpty()) {
            postList.forEach { post ->
                PostItem(post)
            }
        } else {
            EmptyPostMessage()
        }
    }
}


@Composable
fun PostItem(post: Post) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        HorizontalDivider(color = Color.LightGray)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = post.category.name,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = post.title,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = post.content,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (post.postImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.postImageUrl[0])
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = stringResource(id = R.string.profile_image),
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = TimeFormat().toFormattedString(post.timestamp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = post.authorName ?: "",
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = stringResource(id = R.string.likes),
                    modifier = Modifier
                        .size(18.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${post.likes}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AddPostFloatingButton(
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        onClick = {
            onClick()
        },
        icon = {
            Icon(Icons.Filled.Add, "글 쓰기")
        },
        text = {
            Text(text = "글 쓰기")
        },
    )
}