package com.meezzi.localtalk.ui.postdetail

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.Comment
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.ui.common.NavigationTopAppBar

@Composable
fun PostDetailScreen(
    city: String,
    categoryId: String,
    postId: String,
    postDetailViewModel: PostDetailViewModel,
    onNavigateBack: () -> Unit,
    onImageClick: (Int) -> Unit,
) {
    val post by postDetailViewModel.post.collectAsState()
    val profileImage by postDetailViewModel.profileImage.collectAsState()
    val isLiked by postDetailViewModel.isLiked.collectAsState()
    val likeCount by postDetailViewModel.likeCount.collectAsState()
    val isCommentAnonymous by postDetailViewModel.isCommentAnonymous.collectAsState()
    val commentContent by postDetailViewModel.commentContent.collectAsState()
    val comments by postDetailViewModel.comments.collectAsState()
    val errorMessage by postDetailViewModel.errorMessage.collectAsState()

    LaunchedEffect(post) {
        postDetailViewModel.loadPost(postId, city, categoryId)
        post?.authorId?.let { postDetailViewModel.getProfileImage(authorId = it) }
    }

    Scaffold(
        topBar = {
            NavigationTopAppBar(
                title = post?.category?.name,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            CommentInputBottomBar(
                isCommentAnonymous = isCommentAnonymous,
                commentContent = commentContent,
                onCommentAnonymousChange = { postDetailViewModel.updateCommentAnonymous(it) },
                onContentChange = { postDetailViewModel.updateCommentContent(it) },
                onCommentSubmit = { comment, isAnonymous ->
                    postDetailViewModel.saveComment(
                        city,
                        categoryId,
                        postId,
                        post?.authorId!!,
                        post?.authorName!!,
                        comment,
                        isAnonymous
                    )
                    postDetailViewModel.getComments(city, categoryId, postId)
                }
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
                        isLiked = isLiked,
                        likeCount = likeCount,
                        comments = comments,
                        onLikeClick = {
                            postDetailViewModel.togglePostLike(postId, city, categoryId)
                        },
                        onImageClick = onImageClick,
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
fun PostContentView(
    post: Post,
    profileImage: Uri?,
    isLiked: Boolean,
    likeCount: Int,
    comments: List<Comment>,
    onLikeClick: () -> Unit,
    onImageClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        item {
            PostAuthorInfo(post, post.isAnonymous, profileImage)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            PostTitle(post.title)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            PostContent(post.content)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            PostImages(post.postImageUrl, onImageClick)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            PostStats(isLiked, likeCount, onLikeClick)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(comments) { comment ->
            CommentItem(profileImage, comment)
        }
    }
}

@Composable
fun PostAuthorInfo(
    post: Post,
    isAnonymous: Boolean,
    profileImage: Uri?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = if (isAnonymous) R.drawable.ic_user else profileImage,
            contentDescription = stringResource(id = R.string.profile_image),
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

@Composable
fun PostTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
fun PostContent(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun PostImages(
    imageUrls: List<String>?,
    onImageClick: (Int) -> Unit,
) {
    if (!imageUrls.isNullOrEmpty()) {
        LazyRow {
            itemsIndexed(imageUrls) { index, imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(id = R.string.post_image),
                    modifier = Modifier
                        .size(200.dp)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onImageClick(index)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun PostStats(
    isLiked: Boolean,
    likeCount: Int,
    onLikeClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
    ) {
        Text(
            text = "${stringResource(id = R.string.likes)} $likeCount",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.weight(1f))

        FavoriteButton(isLiked = isLiked, onLikeClick = onLikeClick)
    }
}

@Composable
fun FavoriteButton(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
) {

    Row {
        Button(
            onClick = onLikeClick,
            modifier = Modifier.height(35.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLiked) Color.Red else Color.LightGray
            ),
        ) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = stringResource(id = R.string.like_button_label)
            )

            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

            Text(stringResource(id = R.string.like_button_label))
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun CommentInputBottomBar(
    isCommentAnonymous: Boolean,
    commentContent: String,
    onCommentAnonymousChange: (Boolean) -> Unit,
    onContentChange: (String) -> Unit,
    onCommentSubmit: (String, Boolean) -> Unit,
) {

    BottomAppBar(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp),
        ) {
            AnonymousCheckBox(isCommentAnonymous, onCommentAnonymousChange)

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.anonymous),
                modifier = Modifier.clickable { onCommentAnonymousChange(!isCommentAnonymous) },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCommentAnonymous) Color.Red else Color.Gray,
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = commentContent,
                onValueChange = onContentChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.input_comment),
                        color = Color.Black.copy(alpha = 0.3f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Gray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )

            CommentSubmitButton(isCommentAnonymous, commentContent, onCommentSubmit)
        }
    }
}

@Composable
private fun AnonymousCheckBox(
    isCommentAnonymous: Boolean,
    onCommentAnonymousChange: (Boolean) -> Unit
) {
    Checkbox(
        checked = isCommentAnonymous,
        onCheckedChange = { onCommentAnonymousChange(!isCommentAnonymous) },
        modifier = Modifier.size(20.dp),
        colors = CheckboxColors(
            checkedCheckmarkColor = Color.White,
            uncheckedCheckmarkColor = Color.Transparent,
            checkedBoxColor = Color.Red,
            uncheckedBoxColor = Color.LightGray.copy(alpha = 0.3f),
            checkedBorderColor = Color.Red,
            uncheckedBorderColor = Color.LightGray,
            disabledCheckedBoxColor = Color.Transparent,
            disabledUncheckedBoxColor = Color.Transparent,
            disabledIndeterminateBoxColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            disabledUncheckedBorderColor = Color.Transparent,
            disabledIndeterminateBorderColor = Color.Transparent
        ),
    )
}

@Composable
private fun CommentSubmitButton(
    isCommentAnonymous: Boolean,
    commentContent: String,
    onCommentSubmit: (String, Boolean) -> Unit,
) {
    IconButton(
        onClick = {
            if (commentContent.isNotEmpty()) {
                onCommentSubmit(commentContent, isCommentAnonymous)
            }
        },
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = stringResource(id = R.string.input_comment),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CommentItem(
    profileImage: Uri?,
    comment: Comment,
) {

    Row {
        AsyncImage(
            model = if (comment.authorName == stringResource(id = R.string.anonymous)) R.drawable.ic_user else profileImage,
            contentDescription = stringResource(id = R.string.profile_image),
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = comment.authorName,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${comment.date} ${comment.time}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.clickable { }, verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = stringResource(id = R.string.likes),
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${stringResource(id = R.string.likes)} ${comment.likes}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}