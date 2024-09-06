package com.meezzi.localtalk.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _nickname = MutableStateFlow("닉네임")
    val nickname: StateFlow<String> = _nickname

    private val _region = MutableStateFlow("닉네임")
    val region: StateFlow<String> = _region

    private val _profileImageUrl = MutableStateFlow("닉네임")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _isNicknameValid = MutableStateFlow(false)
    val isNicknameValid: StateFlow<Boolean> = _isNicknameValid

    private val _nicknameErrorMessage = MutableStateFlow<String?>(null)
    val nicknameErrorMessage: StateFlow<String?> = _nicknameErrorMessage

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

    fun onNicknameChange(newNickname: String) {
        _nickname.value = newNickname
        validateNickname(newNickname)
    }

    private fun validateNickname(nickname: String) {
        viewModelScope.launch {
            val isValidLength =nickname.length in 2..15
            val isDuplicate = if (isValidLength) userRepository.isNicknameDuplicate(nickname) else false

            _isNicknameValid.value = isValidLength && !isDuplicate
            _nicknameErrorMessage.value = when {
                !isValidLength -> "닉네임은 2글자 이상 15글자 이내여야 합니다."
                isDuplicate -> "이미 사용 중인 닉네임입니다."
                else -> null
            }
        }
    }

    private fun loadUserProfile() {
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