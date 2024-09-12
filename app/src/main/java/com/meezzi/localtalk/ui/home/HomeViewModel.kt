package com.meezzi.localtalk.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.meezzi.localtalk.repository.PermissionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val permissionRepository: PermissionRepository) : ViewModel() {

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    init {
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        viewModelScope.launch {
            _locationPermissionGranted.value = permissionRepository.hasLocationPermission()
        }
    }

    companion object {
        fun provideFactory(repository: PermissionRepository) = viewModelFactory {
            initializer {
                HomeViewModel(repository)
            }
        }
    }
}