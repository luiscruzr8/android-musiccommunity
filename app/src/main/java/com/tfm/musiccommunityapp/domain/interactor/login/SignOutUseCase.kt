package com.tfm.musiccommunityapp.domain.interactor.login

import com.tfm.musiccommunityapp.domain.repository.AuthRepository
import com.tfm.musiccommunityapp.domain.repository.SignOutStatus
import com.tfm.musiccommunityapp.domain.model.GenericError as DomainGenericError

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