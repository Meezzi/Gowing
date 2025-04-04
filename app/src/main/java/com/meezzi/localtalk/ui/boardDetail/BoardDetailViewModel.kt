package com.meezzi.localtalk.ui.boardDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.BoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val boardRepository: BoardRepository
) : ViewModel() {

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage

    fun fetchPostsByCategory(city: String, category: String) {
        viewModelScope.launch {
            boardRepository.fetchPostsByCategory(
                city, category, onSuccess = { posts ->
                    _postList.value = posts
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "알 수 없는 오류가 발생하였습니다."
                    _isLoading.value = false
                }
            )
        }
    }

    fun fetchMyPosts() {
        viewModelScope.launch {
            boardRepository.fetchMyPosts(
                onSuccess = { posts ->
                    _postList.value = posts
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "알 수 없는 오류가 발생하였습니다."
                    _isLoading.value = false
                },
            )
        }
    }

    fun fetchPostsWithMyComments() {
        viewModelScope.launch {
            boardRepository.fetchPostsWithMyComments(
                onSuccess = { posts ->
                    _postList.value = posts
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _errorMessage.value = exception.message ?: "알 수 없는 오류가 발생하였습니다."
                    _isLoading.value = false
                }
            )
        }
    }
}