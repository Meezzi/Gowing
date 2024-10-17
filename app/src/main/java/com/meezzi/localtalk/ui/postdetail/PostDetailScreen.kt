package com.meezzi.localtalk.ui.postdetail

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.ui.common.NavigationTopAppBar

@Composable
fun PostDetailScreen(
    city: String,
    category: String,
    postId: String,
    postDetailViewModel: PostDetailViewModel,
    onNavigateBack: () -> Unit
) {
    val post by postDetailViewModel.post.collectAsState()
    val profileImage by postDetailViewModel.profileImage.collectAsState()
    val errorMessage by postDetailViewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = post?.category?.name,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                errorMessage != null -> {
                    ErrorView(
                        message = errorMessage ?: stringResource(R.string.error_message_not_found)
                    )
                }

                post == null -> {
                    LoadingView()
                }

                else -> {
                    PostContentView(
                        post = post!!,
                        profileImage = profileImage,
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun PostContentView(post: Post, profileImage: Uri?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PostAuthorInfo(post = post, profileImage = profileImage)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PostAuthorInfo(post: Post, profileImage: Uri?) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImage ?: R.drawable.ic_user,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = post.authorName ?: stringResource(id = R.string.anonymous),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${post.date} ${post.time}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}