package com.example.passwordmanager.repository

import com.example.passwordmanager.database.PasswordDao
import com.example.passwordmanager.database.PasswordEntry

class PasswordRepository(private val passwordDao: PasswordDao) {
    suspend fun insert(passwordEntry: PasswordEntry) = passwordDao.insert(passwordEntry)
    suspend fun getAllPasswords() = passwordDao.getAllPasswords()
    suspend fun update(passwordEntry: PasswordEntry) = passwordDao.update(passwordEntry)
    suspend fun delete(passwordEntry: PasswordEntry) = passwordDao.delete(passwordEntry)
}
