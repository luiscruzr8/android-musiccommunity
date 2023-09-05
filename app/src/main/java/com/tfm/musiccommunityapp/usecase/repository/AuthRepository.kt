package com.tfm.musiccommunityapp.usecase.repository

enum class SignInStatus {
    SUCCESS,
    AUTH_ERROR,
    NETWORK_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

enum class SignUpStatus {
    SUCCESS,
    BAD_REQUEST,
    AUTH_ERROR,
    NETWORK_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}

enum class SignOutStatus {
    SUCCESS
}

interface AuthRepository {

    fun isSignedIn(): Boolean

    suspend fun refreshLogin(): SignInStatus

    suspend fun signIn(username: String, password: String, firebaseToken: String): SignInStatus

    suspend fun signUp(
        username: String,
        password: String,
        email: String,
        phoneNumber: String,
        firebaseToken: String
    ): SignUpStatus

    suspend fun logout(): SignOutStatus
}