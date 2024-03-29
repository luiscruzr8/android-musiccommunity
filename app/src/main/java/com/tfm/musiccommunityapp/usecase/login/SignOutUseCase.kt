package com.tfm.musiccommunityapp.usecase.login

import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.SignOutStatus

interface SignOutUseCase {
    suspend operator fun invoke(): SignOutUseCaseResult
}

class SignOutUseCaseImpl(
    private val authRepository: AuthRepository
) : SignOutUseCase {
    override suspend fun invoke(): SignOutUseCaseResult {
        return when (authRepository.logout()) {
            SignOutStatus.SUCCESS -> SignOutUseCaseResult.Success
        }
    }
}

sealed class SignOutUseCaseResult {
    object Success : SignOutUseCaseResult()
}