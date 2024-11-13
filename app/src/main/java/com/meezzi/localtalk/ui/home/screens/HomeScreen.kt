package com.meezzi.localtalk.ui.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    homeViewModel.getAddress()

    Scaffold(
        topBar = {
            CustomTopAppBar(address)
        },
    ) { innerPadding ->
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