package com.meezzi.localtalk.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.ChatRepository

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    companion object {
        fun provideFactory(repository: ChatRepository) = viewModelFactory {
            initializer {
                ChatViewModel(repository)
            }
        }
    }
}