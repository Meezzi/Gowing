package com.meezzi.localtalk.ui.addPost

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.meezzi.localtalk.R

@Composable
fun AddPostScreen(
    addPostViewModel: AddPostViewModel,
    onNavigationBack: () -> Unit,
    onSavePost: () -> Unit
) {
    val selectedImageUris by addPostViewModel.selectedImageUris.collectAsState()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        addPostViewModel.updateSelectedImageUris(uris)
    }

    Scaffold(
        topBar = {
            AddPostTopAppBar(onNavigationBack, onSavePost)
        },
        bottomBar = {
            AddPostBottomAppBar(
                onImageAdd = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )
        }
    ) { innerPadding ->
        Content(innerPadding, selectedImageUris) {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPostTopAppBar(
    onNavigationBack: () -> Unit,
    onSavePost: () -> Unit
) {
    TopAppBar(
        title = {
            Text(stringResource(R.string.write_post))
        },
        navigationIcon = {
            IconButton(onClick = { onNavigationBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back)
                )
            }
        },
        actions = {
            TextButton(onClick = { onSavePost() }) {
                Text(
                    text = stringResource(R.string.complete),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    )
}

@Composable
fun Content(
    innerPadding: PaddingValues,
    selectedImageUris: List<Uri>,
    onSelectBoard: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        BoardSelector(onSelectBoard)

        CustomTextField(
            label = stringResource(R.string.add_post_title_label),
            title = title,
            textStyle = TextStyle(
                fontSize = 23.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            ),
            onTitleChange = { title = it }
        )

        CustomTextField(
            label = stringResource(R.string.add_post_content_label),
            title = content,
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.Black,
            ),
            onTitleChange = { content = it })

        SelectedImagesRow(selectedImageUris)
    }
}

@Composable
private fun SelectedImagesRow(selectedImageUris: List<Uri>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(selectedImageUris) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .padding(5.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun BoardSelector(onSelectBoard: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
            .clickable {
                onSelectBoard()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.add_post_select_board),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = stringResource(R.string.action_back)
        )
    }
}

@Composable
fun CustomTextField(
    label: String,
    title: String,
    textStyle: TextStyle,
    onTitleChange: (String) -> Unit
) {
    BasicTextField(
        value = title,
        onValueChange = onTitleChange,
        textStyle = textStyle,
        modifier = Modifier
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
            .fillMaxWidth(),
        cursorBrush = SolidColor(Color.Gray),
        decorationBox = { innerTextField ->
            Box {
                if (title.isEmpty()) {
                    Text(
                        text = label,
                        style = TextStyle(fontSize = 20.sp, color = Color.LightGray)
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun AddPostBottomAppBar(
    onImageAdd: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(100.dp),
        actions = {
            IconButton(
                onClick = {
                    onImageAdd()
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_image),
                        contentDescription = stringResource(id = R.string.add_post_image),
                        tint = Color.Gray,
                    )
                }
            }
        },
    )
}