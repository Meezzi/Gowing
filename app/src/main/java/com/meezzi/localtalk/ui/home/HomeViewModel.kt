package com.meezzi.localtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.PostSaveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val postSaveRepository: PostSaveRepository,
) : ViewModel() {

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _hotPostList = MutableStateFlow<List<Post>>(emptyList())
    val hotPostList: StateFlow<List<Post>> = _hotPostList.asStateFlow()

    private val _latestPostList = MutableStateFlow<List<Post>>(emptyList())
    val latestPostList: StateFlow<List<Post>> = _latestPostList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getAddress() {
        viewModelScope.launch {
            _address.value = homeRepository.getCurrentLocation()
        }
    }

    fun getHotPostList() {
        viewModelScope.launch {
            postSaveRepository.getHotPosts(
                city = address.value.substringBefore(" "),
                onSuccess = { postList ->
                    _hotPostList.value = postList
                    _isLoading.value = false
                },
            )
        }
    }

    fun getLatestPostList() {
        viewModelScope.launch {
            postSaveRepository.getLatestPosts(
                city = address.value.substringBefore(" "),
                onSuccess = { postList ->
                    _latestPostList.value = postList
                    _isLoading.value = false
                },
            )
        }
    }
}