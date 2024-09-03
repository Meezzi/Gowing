package com.meezzi.localtalk.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.meezzi.localtalk.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IntroViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState

    fun signInWithGoogle() {

        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogle()
                _authState.value = user
            } catch (e: Exception) {

            }
        }
    }

    fun signOutWithGoogle() {

        viewModelScope.launch {
            authRepository.signOutWithGoogle()
            _authState.value = null
        }
    }

    companion object {
        fun provideFactory(repository: AuthRepository) = viewModelFactory {
            initializer {
                IntroViewModel(repository)
            }
        }
    }
}