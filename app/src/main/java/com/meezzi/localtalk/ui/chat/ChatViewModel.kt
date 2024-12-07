package com.meezzi.localtalk.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _currentUserId = MutableStateFlow("")
    val currentUserId = _currentUserId

    init {
        setCurrentUserId()
    }

    private fun setCurrentUserId() {
        _currentUserId.value = chatRepository.getCurrentUserId()
    }

    companion object {
        fun provideFactory(repository: ChatRepository) = viewModelFactory {
            initializer {
                ChatViewModel(repository)
            }
        }
    }
}