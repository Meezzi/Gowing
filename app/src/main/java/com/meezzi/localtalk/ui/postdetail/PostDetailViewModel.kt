package com.meezzi.localtalk.ui.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.PostSaveRepository

class PostDetailViewModel(private val postSaveRepository: PostSaveRepository) : ViewModel() {

    companion object {
        fun provideFactory(repository: PostSaveRepository) = viewModelFactory {
            initializer {
                PostDetailViewModel(repository)
            }
        }
    }
}