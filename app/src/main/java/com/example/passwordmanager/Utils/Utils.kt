package com.example.passwordmanager.Utils

import androidx.compose.ui.graphics.Color

object Utils {

    enum class PasswordStrength(val displayText: String, val color: Color) {
        WEAK("Weak", Color.Red),
        MEDIUM("Medium", Color.Yellow),
        STRONG("Strong", Color.Blue),
        VERY_STRONG("Very Strong", Color.Green)
    }

    fun calculatePasswordStrength(password: String): PasswordStrength {
        var score = 0

        if (password.length >= 8) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { "!@#\$%^&*()_+[]{}|;:'\",.<>?".contains(it) }) score++

        return when (score) {
            in 0..1 -> PasswordStrength.WEAK
            2 -> PasswordStrength.MEDIUM
            3 -> PasswordStrength.STRONG
            else -> PasswordStrength.VERY_STRONG
        }
    }

}