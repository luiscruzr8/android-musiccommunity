package com.tfm.musiccommunityapp.usecase.login

import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.usecase.repository.AuthRepository
import com.tfm.musiccommunityapp.usecase.repository.UserProfileRepository

interface GetCurrentUserUseCase {
    suspend operator fun invoke(): GetCurrentUserResult
}

class GetCurrentUserUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userProfileRepository: UserProfileRepository,
) : GetCurrentUserUseCase {
    override suspend fun invoke(): GetCurrentUserResult {
        return if (!authRepository.isSignedIn()) {
            GetCurrentUserResult.NoUser
        } else {
            userProfileRepository.getUserInfo(null).fold(
                ifLeft = { toErrorResult() },
                ifRight = { it.let { GetCurrentUserResult.Success(it) } }
            )
        }
    }
}

private fun toErrorResult() = GetCurrentUserResult.NoUser

sealed interface GetCurrentUserResult {
    data class Success(val user: UserDomain?) : GetCurrentUserResult
    object NoUser : GetCurrentUserResult
}