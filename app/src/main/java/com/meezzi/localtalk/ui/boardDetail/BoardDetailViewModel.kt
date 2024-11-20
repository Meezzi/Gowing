package com.meezzi.localtalk.ui.boardDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.BoardRepository

class BoardDetailViewModel(private val boardRepository: BoardRepository) : ViewModel() {

    companion object {
        fun provideFactory(
            boardRepository: BoardRepository,
        ) = viewModelFactory {
            initializer {
                BoardDetailViewModel(boardRepository)
            }
        }
    }
}