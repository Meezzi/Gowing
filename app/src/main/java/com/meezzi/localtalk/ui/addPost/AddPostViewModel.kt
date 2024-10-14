package com.meezzi.localtalk.ui.addPost

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.Categories
import com.meezzi.localtalk.data.CategorySection
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.PostSaveRepository
import com.meezzi.localtalk.ui.home.HomeViewModel
import com.meezzi.localtalk.util.TimeFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val postSaveRepository: PostSaveRepository,
    private val homeViewModel: HomeViewModel,
) : ViewModel() {

    val categories: List<CategorySection> = Categories.entries.map { categoryType ->
        CategorySection(id = categoryType.name.lowercase(), name = categoryType.displayName)
    }

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

    fun savePost(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val category = selectedCategory.value
        val address = homeViewModel.address.value.split(" ")[0]

        if (category != null && address.isNotEmpty()) {
            viewModelScope.launch {
                val post = Post(
                    city = address,
                    category = category,
                    postId = "",
                    title = title.value,
                    content = content.value,
                    authorId = null,
                    time = TimeFormat().timeFormat(),
                    postImageUrl = selectedImageUris.value.map { it.toString() },
                    likes = 0,
                    comments = null
                )

                postSaveRepository.savePostWithImages(
                    post = post,
                    imageUris = selectedImageUris.value,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: PostSaveRepository,
            homeViewModel: HomeViewModel
        ) = viewModelFactory {
            initializer {
                AddPostViewModel(repository, homeViewModel)
            }
        }
    }
}