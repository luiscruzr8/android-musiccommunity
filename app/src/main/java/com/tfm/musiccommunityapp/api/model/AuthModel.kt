package com.tfm.musiccommunityapp.api.model



data class SignInBody(
    val login: String,
    val password: String,
    val firebaseToken: String,
)

data class SignUpBody(
    val login: String,
    val email: String,
    val password: String,
    val phone: String,
    val firebaseToken: String,
) {
    val role: List<String> = listOf("user")
}

data class SignUpResponse(
    val message: String
)

data class SignInResponse(
    val username: String,
    val authorities: List<Authority>,
    val accessToken: String,
    val tokenType: String
)

data class Authority(
    val authority: String
)