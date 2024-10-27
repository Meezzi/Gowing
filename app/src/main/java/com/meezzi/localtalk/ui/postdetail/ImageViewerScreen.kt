package com.meezzi.localtalk.ui.postdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R

@Composable
fun ImageViewerScreen(
    postDetailViewModel: PostDetailViewModel,
    onDismiss: () -> Unit,
) {
    val imageUrls by postDetailViewModel.imageList.collectAsState(emptyList())
    val selectedImageIndex by postDetailViewModel.selectedImageIndex.collectAsState(0)

    val pagerState = rememberPagerState(
        initialPage = selectedImageIndex,
        pageCount = { imageUrls.size }
    )

    LaunchedEffect(selectedImageIndex) {
        pagerState.scrollToPage(selectedImageIndex)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = stringResource(id = R.string.post_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }

        CloseButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 32.dp),
            onDismiss = onDismiss,
        )

        ImageIndicators(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            pagerState = pagerState,
            imageCount = imageUrls.size,
        )
    }
}

@Composable
private fun CloseButton(
    modifier: Modifier,
    onDismiss: () -> Unit,
) {
    IconButton(
        onClick = onDismiss,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(id = R.string.close),
            tint = Color.White,
        )
    }
}

@Composable
fun ImageIndicators(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    imageCount: Int,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(imageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp),
            )
        }
    }
}