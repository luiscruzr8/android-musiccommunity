package com.tfm.musiccommunityapp.usecase.login

import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.SignInStatus
import com.tfm.musiccommunityapp.domain.model.GenericError as DomainGenericError

interface SignInUseCase {
    suspend operator fun invoke(
        username: String,
        password: String,
        firebaseToken: String
    ): SignInUseCaseResult
}

class SignInUseCaseImpl(
    private val authRepository: AuthRepository
) : SignInUseCase {
    override suspend fun invoke(
        username: String,
        password: String,
        firebaseToken: String
    ): SignInUseCaseResult {
        return when (authRepository.signIn(username, password, firebaseToken)) {
            SignInStatus.SUCCESS -> SignInUseCaseResult.Success
            SignInStatus.AUTH_ERROR -> SignInUseCaseResult.GenericError(
                DomainGenericError(
                    "Authentication error",
                    "Incorrect username or password",
                    401
                )
            )

            SignInStatus.NETWORK_ERROR -> SignInUseCaseResult.GenericError(
                DomainGenericError(
                    "Network error",
                    "Make sure you have a stable internet connection and try again later",
                    504
                )
            )
            SignInStatus.SERVER_ERROR -> SignInUseCaseResult.GenericError(
                DomainGenericError(
                    "Server error",
                    "Internal server error",
                    500
                )
            )
            SignInStatus.UNKNOWN_ERROR -> SignInUseCaseResult.GenericError(
                DomainGenericError(
                    "Unknown error",
                    "Something went wrong trying to sign in",
                    999
                )
            )
        }
    }
}

sealed interface SignInUseCaseResult {
    object Success : SignInUseCaseResult
    data class GenericError(val error: DomainGenericError) : SignInUseCaseResult
}