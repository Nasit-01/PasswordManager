package com.example.passwordmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}
