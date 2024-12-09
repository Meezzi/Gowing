package com.meezzi.localtalk.ui.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.ChatRoom
import com.meezzi.localtalk.data.Message
import com.meezzi.localtalk.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

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

    private val _chatRoomList = MutableStateFlow<List<ChatRoom>>(emptyList())
    val chatRoomList = _chatRoomList

    init {
        setCurrentUserId()
    }

    private fun setCurrentUserId() {
        _currentUserId.value = chatRepository.getCurrentUserId()
    }

    fun updateChatContent(newContent: String) {
        _chatContent.value = newContent
    }

    fun getOrCreateChatRoom(authorId: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            chatRepository.getOrCreateChatRoom(authorId) { chatRoomId ->
                onResult(chatRoomId)
            }
        }
    }

    fun sendMessage(chatRoomId: String, messageContent: String, imageUrls: List<String> = emptyList()) {
        viewModelScope.launch {
            chatRepository.sendMessage(chatRoomId, messageContent, imageUrls)
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
            chatRepository.fetchProfileImageByUserId(chatRoomId) { uri->
                _profileImageUri.value = uri
            }
        }
    }

    fun sendImages(chatRoomId: String, urls: List<Uri>) {
        viewModelScope.launch {
            val imageUrls = urls.map { url ->
                chatRepository.uploadImageToFirebase(chatRoomId, url)
            }
            sendMessage(chatRoomId, "[사진]", imageUrls)
        }
    }

    fun fetchChatRoomList() {
        viewModelScope.launch {
                _chatRoomList.value = chatRepository.fetchChatRoomList()
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