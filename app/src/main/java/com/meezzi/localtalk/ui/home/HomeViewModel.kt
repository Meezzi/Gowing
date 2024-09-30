package com.meezzi.localtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    fun getAddress() {
        viewModelScope.launch {
            _address.value = homeRepository.getCurrentLocation()
        }
    }

    companion object {
        fun provideFactory(repository: HomeRepository) = viewModelFactory {
            initializer {
                HomeViewModel(repository)
            }
        }
    }
}