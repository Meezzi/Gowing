package com.meezzi.localtalk.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatContent = MutableStateFlow("")
    val chatContent = _chatContent

    private val _currentUserId = MutableStateFlow("")
    val currentUserId = _currentUserId

    init {
        setCurrentUserId()
    }

    private fun setCurrentUserId() {
        _currentUserId.value = chatRepository.getCurrentUserId()
    }

    fun updateChatContent(newContent: String) {
        _chatContent.value = newContent
    }

    fun sendMessage(chatRoomId: String, messageContent: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(chatRoomId, messageContent)
            updateChatContent("")
        }
    }

    companion object {
        fun provideFactory(repository: ChatRepository) = viewModelFactory {
            initializer {
                ChatViewModel(repository)
            }
        }
    }
}