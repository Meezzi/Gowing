package com.meezzi.localtalk.ui.addPost

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.meezzi.localtalk.R
import com.meezzi.localtalk.data.CategorySection
import com.meezzi.localtalk.ui.common.CustomPermissionRationaleDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    addPostViewModel: AddPostViewModel = hiltViewModel(),
    onNavigationBack: () -> Unit,
    onSavePost: (String, String, String) -> Unit
) {

    val context = LocalContext.current

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var showPermissionRationaleDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet by addPostViewModel.showBottomSheet.collectAsState()

    val selectedCategory by addPostViewModel.selectedCategory.collectAsState()
    val categories = addPostViewModel.categories
    val title by addPostViewModel.title.collectAsState()
    val content by addPostViewModel.content.collectAsState()
    val selectedImageUris by addPostViewModel.selectedImageUris.collectAsState()

    val isSaveEnabled = title.isNotBlank() && content.isNotBlank() && selectedCategory != null

    val isAnonymous by addPostViewModel.isAnonymous.collectAsState()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        val contentResolver = context.contentResolver
        uris.forEach { uri ->
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        addPostViewModel.updateSelectedImageUris(uris)
    }

    val mediaPermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                showPermissionRationaleDialog = true
            }
        }

    LaunchedEffect(Unit) {
        addPostViewModel.updateAddress()
    }

    Scaffold(
        topBar = {
            AddPostTopAppBar(
                isSaveEnabled = isSaveEnabled,
                onNavigationBack = onNavigationBack,
                onSavePost = {
                    addPostViewModel.savePost(
                        onSuccess = { city, categoryId, postId ->
                            onSavePost(city, categoryId, postId)
                            addPostViewModel.clearPostData()
                        },
                        onFailure = { e ->
                            // TODO("실패 원인 띄우기")
                        }
                    )
                }
            )
        },
        bottomBar = {
            AddPostBottomAppBar(
                isAnonymous = isAnonymous,
                onAnonymousChange = { addPostViewModel.setAnonymous(it) },
                onImageAdd = {
                    mediaPermission.launch(permission)
                }
            )
        }
    ) { innerPadding ->

        if (showPermissionRationaleDialog) {
            CustomPermissionRationaleDialog(
                text = stringResource(R.string.permission_dialog_media_content),
                onDismiss = { showPermissionRationaleDialog = false },
                onConfirm = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
            )
        }

        Content(
            innerPadding,
            selectedCategory = selectedCategory,
            title,
            content,
            selectedImageUris,
            onTitleChange = { addPostViewModel.updateTitle(it) },
            onContentChange = { addPostViewModel.updateContent(it) },
            onSelectBoard = { addPostViewModel.setShowBottomSheet(true) }
        )

        if (showBottomSheet) {

            CategorySelectionBottomSheet(
                sheetState = sheetState,
                categories = categories,
                onCategorySelected = { category ->
                    addPostViewModel.selectCategory(category)
                    addPostViewModel.setShowBottomSheet(false)
                },
                onDismissRequest = { addPostViewModel.setShowBottomSheet(false) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionBottomSheet(
    sheetState: SheetState,
    categories: List<CategorySection>,
    onCategorySelected: (CategorySection) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        CategoryList(
            categories = categories,
            onCategorySelected = { category ->
                onCategorySelected(category)
                onDismissRequest()
            }
        )
    }
}

@Composable
fun CategoryList(
    categories: List<CategorySection>,
    onCategorySelected: (CategorySection) -> Unit,
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text(
                text = stringResource(R.string.add_post_select_board),
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        items(categories) { category ->
            Text(
                text = category.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCategorySelected(category)
                    }
                    .padding(vertical = 12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPostTopAppBar(
    isSaveEnabled: Boolean,
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
            TextButton(
                onClick = { onSavePost() },
                enabled = isSaveEnabled,
            ) {
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
    selectedCategory: CategorySection?,
    title: String,
    content: String,
    selectedImageUris: List<Uri>,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSelectBoard: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

        BoardSelector(
            selectedCategory = selectedCategory,
            onSelectBoard = onSelectBoard
        )

        CustomTextField(
            value = title,
            label = stringResource(R.string.add_post_title_label),
            textStyle = TextStyle(
                fontSize = 23.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            ),
            onValueChange = onTitleChange
        )

        CustomTextField(
            value = content,
            label = stringResource(R.string.add_post_content_label),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.Black,
            ),
            onValueChange = onContentChange
        )

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
private fun BoardSelector(
    selectedCategory: CategorySection?,
    onSelectBoard: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable { onSelectBoard() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedCategory?.name ?: stringResource(R.string.add_post_select_board_title),
            color = Color.Gray,
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            imageVector = Icons.Outlined.ArrowDropDown,
            contentDescription = stringResource(R.string.add_post_select_board_title)
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    label: String,
    textStyle: TextStyle,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(start = 20.dp, top = 5.dp, end = 20.dp, bottom = 5.dp)
            .fillMaxWidth(),
        textStyle = textStyle,
        cursorBrush = SolidColor(Color.Gray),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
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
    isAnonymous: Boolean,
    onAnonymousChange: (Boolean) -> Unit,
    onImageAdd: () -> Unit
) {
    BottomAppBar(
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

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isAnonymous,
                    onCheckedChange = onAnonymousChange,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.anonymous),
                    modifier = Modifier.clickable { onAnonymousChange(!isAnonymous) },
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        },
        modifier = Modifier.height(100.dp),
    )
}