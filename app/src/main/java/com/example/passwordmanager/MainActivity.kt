package com.example.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.passwordmanager.Utils.EncryptionUtils
import com.example.passwordmanager.database.AppDatabase
import com.example.passwordmanager.repository.PasswordRepository
import com.example.passwordmanager.screens.PasswordListScreen
import com.example.passwordmanager.ui.theme.PasswordManagerTheme
import com.example.passwordmanager.viewmodel.PasswordViewModel
import com.example.passwordmanager.viewmodel.PasswordViewModelFactory
import java.security.Key

class MainActivity : ComponentActivity() {
    private val key: Key = EncryptionUtils.generateKey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "passwords-database"
        ).build()
        val repository = PasswordRepository(db.passwordDao())

        val viewModelFactory = PasswordViewModelFactory(repository, key)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(PasswordViewModel::class.java)
        setContent {
            PasswordManagerTheme {
                PasswordListScreen(viewModel)
            }
        }
    }
}
