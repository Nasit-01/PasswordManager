package com.example.passwordmanager.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordmanager.database.PasswordEntry
import com.example.passwordmanager.repository.PasswordRepository
import com.example.passwordmanager.Utils.EncryptionUtils
import kotlinx.coroutines.launch
import java.security.Key

class PasswordViewModel(private val repository: PasswordRepository, private val key: Key) : ViewModel() {
    val passwords = mutableStateListOf<PasswordEntry>()

    init {
        viewModelScope.launch {
            passwords.addAll(repository.getAllPasswords())
        }
    }

    fun addPassword(serviceName: String,emailOrName:String,password: String) {
        viewModelScope.launch {
            val encryptedPassword = EncryptionUtils.encrypt(password, key)
            val passwordEntry = PasswordEntry(serviceName = serviceName,emailOrName = emailOrName, encryptedPassword = encryptedPassword, key = EncryptionUtils.convertKeyToString(key))
            repository.insert(passwordEntry)
            passwords.add(passwordEntry)
        }
    }

    fun updatePassword(passwordEntry: PasswordEntry, newPassword: String) {
        viewModelScope.launch {
            val encryptedPassword = EncryptionUtils.encrypt(newPassword, key)
            val updatedEntry = passwordEntry.copy(encryptedPassword = encryptedPassword)
            repository.update(updatedEntry)
            passwords[passwords.indexOfFirst { it.id == passwordEntry.id }] = updatedEntry
        }
    }

    fun deletePassword(passwordEntry: PasswordEntry) {
        viewModelScope.launch {
            repository.delete(passwordEntry)
            passwords.remove(passwordEntry)
        }
    }
}
