package com.example.passwordmanager.Utils

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import android.util.Base64
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    private const val ALGORITHM = "AES"

    fun generateKey(): Key {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    fun encrypt(value: String, key: Key): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedValue = cipher.doFinal(value.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    fun decrypt(value: String, key: Key): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decryptedValue = cipher.doFinal(Base64.decode(value, Base64.DEFAULT))
        return String(decryptedValue)
    }
    fun convertKeyToString(key: Key): String {
        return Base64.encodeToString(key.encoded, Base64.DEFAULT)
    }
    fun convertStringToKey(keyString: String): Key {
        val keyBytes = Base64.decode(keyString, Base64.DEFAULT)
        return SecretKeySpec(keyBytes, "AES") // Use the appropriate algorithm
    }
}
