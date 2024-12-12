package com.meezzi.localtalk.ui.chat

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
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

    private val _isChatRoomActive = MutableStateFlow(true)
    val isChatRoomActive = _isChatRoomActive

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

    private val _chatRoomInfo =
        MutableStateFlow<List<Pair<ChatRoom, Pair<String, Uri?>>>>(emptyList())
    val chatRoomInfo = _chatRoomInfo

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage

    private val _snackbarHostState = SnackbarHostState()
    val snackbarHostState = _snackbarHostState

    init {
        setCurrentUserId()
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
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

    fun sendMessage(
        chatRoomId: String,
        messageContent: String,
        imageUrls: List<String> = emptyList()
    ) {
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

    fun observeOtherUserNickname(chatRoomId: String) {
        viewModelScope.launch {
            val otherUserId = chatRepository.fetchOtherUserId(chatRoomId)
            chatRepository.observeNickname(otherUserId) { nickname ->
                _userNickname.value = nickname
            }
        }
    }

    fun observeProfileImage(chatRoomId: String) {
        viewModelScope.launch {
            val otherUserId = chatRepository.fetchOtherUserId(chatRoomId)
            chatRepository.observeProfileImage(otherUserId) { uri ->
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

    fun fetchChatRoomListWithDetails() {
        viewModelScope.launch {
            try {
                val chatRooms = chatRepository.fetchChatRoomList()
                val details = chatRooms.map { chatRoom ->
                    val participantDetails = chatRepository.fetchParticipantInfo(chatRoom)
                    chatRoom to participantDetails
                }
                _chatRoomInfo.value = details
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun exitChatRoom(
        chatRoomId: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                chatRepository.deleteChatRoomParticipant(chatRoomId, currentUserId.value)
                _isChatRoomActive.value = false
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "알 수 없는 오류가 발생하였습니다."
            }
        }
    }

    fun updateChatRoomStatus(chatRoomId: String) {
        viewModelScope.launch {
            chatRepository.observeIsActive(chatRoomId) { isActive ->
                _isChatRoomActive.value = isActive
            }
        }
    }

    fun checkAndDeleteIfNoMessage(chatRoomId: String) {
        viewModelScope.launch {
            if(chatRepository.isMessageEmpty(chatRoomId)) {
                chatRepository.deleteChatRoom(chatRoomId)
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