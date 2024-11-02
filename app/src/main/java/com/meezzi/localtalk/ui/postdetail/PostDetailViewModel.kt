package com.meezzi.localtalk.ui.postdetail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.Comment
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.PostSaveRepository
import com.meezzi.localtalk.util.TimeFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(private val postSaveRepository: PostSaveRepository) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage: StateFlow<Uri?> = _profileImage

    private val _isLiked = MutableStateFlow(false)
    val isLiked: StateFlow<Boolean> = _isLiked

    private val _likeCount = MutableStateFlow(0)
    val likeCount: StateFlow<Int> = _likeCount

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _imageList = MutableStateFlow<List<String>>(emptyList())
    val imageList: StateFlow<List<String>> = _imageList

    private val _selectedImageIndex = MutableStateFlow(0)
    val selectedImageIndex: StateFlow<Int> = _selectedImageIndex

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun updateImageList(images: List<String>) {
        _imageList.value = images
    }

    fun updateSelectedImageIndex(index: Int) {
        _selectedImageIndex.value = index
    }

    fun loadPost(
        postId: String,
        city: String,
        categoryId: String,
    ) {
        viewModelScope.launch {
            postSaveRepository.getPostById(
                city = city,
                categoryId = categoryId,
                postId = postId,
                onSuccess = { post ->
                    _post.value = post
                    _isLiked.value = false
                    _likeCount.value = post.likes
                },
                onFailure = { exception ->
                    _post.value = null
                    _errorMessage.value = exception.message
                }
            )
        }
    }

    fun getProfileImage(authorId: String) {
        viewModelScope.launch {
            postSaveRepository.getProfileImageUri(
                authorId = authorId,
                onComplete = { uri ->
                    _profileImage.value = uri
                }
            )
        }
    }

    fun togglePostLike(
        postId: String,
        city: String,
        categoryId: String
    ) {
        viewModelScope.launch {
            if (_isLiked.value) {
                postSaveRepository.minusLikeCount(postId, city, categoryId)
            } else {
                postSaveRepository.plusLikeCount(postId, city, categoryId)
            }
            postSaveRepository.getLikeCount(
                postId = postId,
                city = city,
                categoryId = categoryId,
                onComplete = { likeCount ->
                    _likeCount.value = likeCount
                },
            )
            _isLiked.value = !_isLiked.value
        }
    }

    fun saveComment(
        city: String,
        categoryId: String,
        postId: String,
        authorId: String,
        authorName: String,
        content: String,
        isAnonymous: Boolean,
    ) {
        val comment = Comment(
            postId = postId,
            authorId = authorId,
            authorName = if (isAnonymous) "익명" else authorName,
            date = TimeFormat().getDate(),
            time = TimeFormat().getTime(),
            content = content,
            likes = 0,
        )

        viewModelScope.launch {
            postSaveRepository.saveComment(
                city = city,
                categoryId = categoryId,
                postId = postId,
                comment = comment,
                onSuccess = { },
                onFailure = { _errorMessage.value = it.message },
            )
        }
    }

    fun getComments(
        city: String,
        categoryId: String,
        postId: String,
    ) {

        viewModelScope.launch {
            postSaveRepository.getComments(
                postId = postId,
                city = city,
                categoryId = categoryId,
                onSuccess = { commentList -> _comments.value = commentList },
                onFailure = { e -> _errorMessage.value = e.message }
            )
        }
    }

    companion object {
        fun provideFactory(repository: PostSaveRepository) = viewModelFactory {
            initializer {
                PostDetailViewModel(repository)
            }
        }
    }
}