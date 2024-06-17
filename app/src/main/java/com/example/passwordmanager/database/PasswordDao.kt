package com.example.passwordmanager.database

import androidx.room.*
import androidx.room.OnConflictStrategy

@Dao
interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passwordEntry: PasswordEntry)

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<PasswordEntry>

    @Update
    suspend fun update(passwordEntry: PasswordEntry)

    @Delete
    suspend fun delete(passwordEntry: PasswordEntry)
}
