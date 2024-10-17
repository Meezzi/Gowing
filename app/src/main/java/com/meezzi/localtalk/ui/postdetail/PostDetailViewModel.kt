package com.meezzi.localtalk.ui.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.PostSaveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(private val postSaveRepository: PostSaveRepository) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadPost(
        postId: String,
        city: String,
        category: String,
    ) {
        viewModelScope.launch {
            postSaveRepository.getPostById(
                city = city,
                category = category,
                postId = postId,
                onSuccess = { post ->
                    _post.value = post
                },
                onFailure = { exception ->
                    _post.value = null
                    _errorMessage.value = exception.message
                }
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