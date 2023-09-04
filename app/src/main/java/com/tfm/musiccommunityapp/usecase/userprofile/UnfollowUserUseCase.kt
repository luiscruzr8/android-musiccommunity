package com.tfm.musiccommunityapp.usecase.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.usecase.repository.UserProfileRepository

interface UnfollowUserUseCase {
    suspend operator fun invoke(username: String): UnfollowUserUseCaseResult
}

class UnfollowUserUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : UnfollowUserUseCase {

    override suspend fun invoke(username: String): UnfollowUserUseCaseResult =
        userProfileRepository.unfollowUser(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { UnfollowUserUseCaseResult.Success }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UnfollowUserUseCaseResult.NetworkError(this)
    is NotFoundError -> UnfollowUserUseCaseResult.NotFoundError(this)
    else -> UnfollowUserUseCaseResult.GenericError(this)
}

sealed interface UnfollowUserUseCaseResult {
    object Success: UnfollowUserUseCaseResult
    data class  NetworkError(val error: DomainError): UnfollowUserUseCaseResult
    data class  NotFoundError(val error: DomainError): UnfollowUserUseCaseResult
    data class  GenericError(val error: DomainError): UnfollowUserUseCaseResult
}