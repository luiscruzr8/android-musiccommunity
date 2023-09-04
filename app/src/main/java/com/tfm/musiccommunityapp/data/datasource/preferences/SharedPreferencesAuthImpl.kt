package com.tfm.musiccommunityapp.data.datasource.preferences

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.tfm.musiccommunityapp.data.datasource.AuthDatasource
import com.tfm.musiccommunityapp.ui.base.string

class SharedPreferencesAuthImpl(
    sharedPreferences: SharedPreferences,
    encryptedPreferences: EncryptedSharedPreferences
): AuthDatasource {
    override var encryptedUserName by encryptedPreferences.string(ENCRYPTED_LOGIN_KEY)

    override var encryptedPassword by encryptedPreferences.string(ENCRYPTED_PASSWORD_KEY)

    override var accessToken by sharedPreferences.string(ACCESS_TOKEN)

    override val bearerToken: String
        get() = "Bearer $accessToken"

    override fun clearData() {
        accessToken = ""
        encryptedUserName = ""
        encryptedPassword = ""
    }

    companion object {
        private const val ENCRYPTED_LOGIN_KEY = "encrypted_login_key"
        private const val ENCRYPTED_PASSWORD_KEY = "encrypted_password_key"
        private const val ACCESS_TOKEN = "access_token"
    }
}