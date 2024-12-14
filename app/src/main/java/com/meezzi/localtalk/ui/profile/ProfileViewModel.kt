package com.meezzi.localtalk.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.data.Post
import com.meezzi.localtalk.repository.HomeRepository
import com.meezzi.localtalk.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading

    private val _nickname = MutableStateFlow("닉네임")
    val nickname: StateFlow<String> = _nickname

    private val _region = MutableStateFlow("지역 없음")
    val region: StateFlow<String> = _region

    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    private val _isNicknameValid = MutableStateFlow(false)
    val isNicknameValid: StateFlow<Boolean> = _isNicknameValid

    private val _nicknameErrorMessage = MutableStateFlow<String?>(null)
    val nicknameErrorMessage: StateFlow<String?> = _nicknameErrorMessage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _myPosts = MutableStateFlow<List<Post>>(emptyList())
    val myPosts = _myPosts

    fun loadRegion() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _region.value = homeRepository.getCurrentLocation()
            } catch (e: Exception) {
                _errorMessage.value = "지역 정보를 불러오지 못했습니다."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveUserProfile(nickname: String, profileImage: Uri?) {
        userRepository.saveProfileData(nickname, profileImage) { success ->
            if (success) {
                _nickname.value = nickname
                _profileImageUri.value = profileImage
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
            val isValidLength = nickname.length in 2..15
            val isDuplicate = if (isValidLength) userRepository.isNicknameDuplicate(nickname) else false

            _isNicknameValid.value = isValidLength && !isDuplicate
            _nicknameErrorMessage.value = when {
                !isValidLength -> "닉네임은 2글자 이상 15글자 이내여야 합니다."
                isDuplicate -> "이미 사용 중인 닉네임입니다."
                else -> null
            }
        }
    }

    fun loadUserProfile() {
        userRepository.getProfileData { nickname ->
            _nickname.value = nickname
        }

        userRepository.getProfileImageUri { uri ->
            _profileImageUri.value = uri
        }
    }

    fun loadMyPosts(city: String) {
        viewModelScope.launch {
            try {
                userRepository.fetchMyPosts(
                    city = city,
                    onComplete = { postList ->
                        _myPosts.value = postList.toList()
                        _isLoading.value = false
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "알 수 없는 오류가 발생하였습니다."
                _isLoading.value = false
            }
        }
    }

    companion object {
        fun provideFactory(
            userRepository: UserRepository,
            homeRepository: HomeRepository
        ) = viewModelFactory {
            initializer {
                ProfileViewModel(userRepository, homeRepository)
            }
        }
    }
}