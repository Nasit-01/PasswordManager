package com.example.passwordmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.Key

@Entity(tableName = "passwords")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceName: String,
    val encryptedPassword: String,
    val emailOrName: String,
    val key:String
)
