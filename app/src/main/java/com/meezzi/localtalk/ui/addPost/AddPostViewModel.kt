package com.meezzi.localtalk.ui.addPost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.CategorySection
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.PostSaveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postSaveRepository: PostSaveRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    val categories: List<CategorySection> = Categories.entries.map { categoryType ->
        CategorySection(id = categoryType.name.lowercase(), name = categoryType.displayName)
    }

    private val _address = MutableStateFlow("")
    val address = _address

    private val _selectedCategory = MutableStateFlow<CategorySection?>(null)
    val selectedCategory: StateFlow<CategorySection?> = _selectedCategory

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _selectedImageUris = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImageUris: StateFlow<List<Uri>> = _selectedImageUris

    private val _isAnonymous = MutableStateFlow(false)
    val isAnonymous: StateFlow<Boolean> = _isAnonymous

    fun selectCategory(category: CategorySection) {
        _selectedCategory.value = category
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun updateSelectedImageUris(uris: List<Uri>) {
        _selectedImageUris.value = uris
    }

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    fun setShowBottomSheet(show: Boolean) {
        _showBottomSheet.value = show
    }

    fun setAnonymous(isAnonymous: Boolean) {
        _isAnonymous.value = isAnonymous
    }

    suspend fun updateAddress() {
        _address.value = homeRepository.getCurrentLocation().split(" ")[0]
    }

    fun savePost(
        onSuccess: (String, String, String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val category = selectedCategory.value
        val address = _address.value

        if (category != null) {
            viewModelScope.launch {
                val post = Post(
                    city = address,
                    category = category,
                    postId = "",
                    title = title.value,
                    content = content.value,
                    authorId = "",
                    authorName = "",
                    isAnonymous = isAnonymous.value,
                    timestamp = Timestamp.now(),
                    postImageUrl = selectedImageUris.value.map { it.toString() },
                    likes = 0,
                )

                postSaveRepository.savePostWithImages(
                    post = post,
                    imageUris = selectedImageUris.value,
                    isAnonymous = isAnonymous.value,
                    onSuccess = { city, categoryId, postId ->
                        onSuccess(city, categoryId, postId)
                    },
                    onFailure = onFailure
                )
            }
        }
    }

    fun clearPostData() {
        _title.value = ""
        _content.value = ""
        _selectedImageUris.value = emptyList()
        _selectedCategory.value = null
        _isAnonymous.value = false
    }
}