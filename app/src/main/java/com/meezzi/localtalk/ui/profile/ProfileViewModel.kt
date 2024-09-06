package com.meezzi.localtalk.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _nickname = MutableStateFlow("닉네임")
    val nickname: StateFlow<String> = _nickname

    private val _region = MutableStateFlow("닉네임")
    val region: StateFlow<String> = _region

    private val _profileImageUrl = MutableStateFlow("닉네임")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadUserProfile()
    }

    fun saveUserProfile(nickname: String) {

        userRepository.saveProfileData(nickname) { success ->
            if (success) {
                _nickname.value = nickname
            } else {
                _errorMessage.value = "저장 실패"
            }
        }
    }

    fun loadUserProfile() {
        userRepository.getProfileData { nickname, profileImageUrl ->
            _nickname.value = nickname
            _profileImageUrl.value = profileImageUrl
        }
    }

    companion object {
        fun provideFactory(repository: UserRepository) = viewModelFactory {
            initializer {
                ProfileViewModel(repository)
            }
        }
    }
}