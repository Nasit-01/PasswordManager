package com.example.passwordmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.passwordmanager.repository.PasswordRepository
import java.security.Key

class PasswordViewModelFactory(
    private val repository: PasswordRepository,
    private val key: Key
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            return PasswordViewModel(repository, key) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
