package com.tfm.musiccommunityapp.usecase.login

import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.SignUpStatus
import com.tfm.musiccommunityapp.domain.model.GenericError as DomainGenericError

interface SignUpUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        email: String,
        phone: String,
        firebaseToken: String
    ): SignUpUseCaseResult
}

class SignUpUseCaseImpl(
    private val authRepository: AuthRepository
) : SignUpUseCase {
    override suspend fun invoke(
        username: String,
        password: String,
        email: String,
        phone: String,
        firebaseToken: String
    ): SignUpUseCaseResult {
        return when (authRepository.signUp(username, password, email, phone, firebaseToken)) {
            SignUpStatus.SUCCESS -> SignUpUseCaseResult.Success
            SignUpStatus.AUTH_ERROR -> SignUpUseCaseResult.GenericError(
                DomainGenericError(
                    "Authentication error",
                    "Incorrect username or password",
                    401
                )
            )

            SignUpStatus.BAD_REQUEST -> SignUpUseCaseResult.GenericError(
                DomainGenericError(
                    "Bad request error",
                    "Check your input data and try again",
                    400
                )
            )
            SignUpStatus.NETWORK_ERROR -> SignUpUseCaseResult.GenericError(
                DomainGenericError(
                    "Network error",
                    "Make sure you have a stable internet connection and try again later",
                    504
                )
            )
            SignUpStatus.SERVER_ERROR -> SignUpUseCaseResult.GenericError(
                DomainGenericError(
                    "Server error",
                    "Internal server error",
                    500
                )
            )
            SignUpStatus.UNKNOWN_ERROR -> SignUpUseCaseResult.GenericError(
                DomainGenericError(
                    "Unknown error",
                    "Something went wrong trying to sign up",
                    999
                )
            )
        }
    }
}

sealed class SignUpUseCaseResult {
    object Success : SignUpUseCaseResult()
    data class GenericError(val error: DomainGenericError) : SignUpUseCaseResult()
}