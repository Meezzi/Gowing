package com.meezzi.localtalk.ui.addPost

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meezzi.localtalk.R

@Composable
fun AddPostScreen(
    addPostViewModel: AddPostViewModel,
    onNavigationBack: () -> Unit,
    onSavePost: () -> Unit
) {
    Scaffold(
        topBar = {
            AddPostTopAppBar(onNavigationBack, onSavePost)
        },
        bottomBar = {
            AddPostBottomAppBar {

            }
        }
    ) { innerPadding ->
        Content(innerPadding) {
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