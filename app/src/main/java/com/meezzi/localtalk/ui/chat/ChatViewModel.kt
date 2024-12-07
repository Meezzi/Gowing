package com.meezzi.localtalk.ui.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.Message
import com.meezzi.localtalk.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _userNickname = MutableStateFlow("")
    val userNickname = _userNickname

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri = _profileImageUri

    private val _chatContent = MutableStateFlow("")
    val chatContent = _chatContent

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages

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

    fun fetchMessages(chatRoomId: String) {
        viewModelScope.launch {
            chatRepository.fetchMessages(chatRoomId) { messages ->
                _messages.value = messages
            }
        }
    }

    fun fetchOtherUserNickname(chatRoomId: String) {
        viewModelScope.launch {
            chatRepository.getUserNickname(chatRoomId) { nickname ->
                _userNickname.value = nickname
            }
        }
    }

    fun fetchProfileImageUri(chatRoomId: String) {
        viewModelScope.launch {
            chatRepository.fetchProfileImageByUserId(chatRoomId) { uri ->
                _profileImageUri.value = uri
            }
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