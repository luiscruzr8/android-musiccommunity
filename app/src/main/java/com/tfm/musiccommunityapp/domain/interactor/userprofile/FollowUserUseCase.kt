package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface FollowUserUseCase {
    suspend operator fun invoke(username: String): FollowUserUseCaseResult
}

class FollowUserUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : FollowUserUseCase {

    override suspend fun invoke(username: String): FollowUserUseCaseResult =
        userProfileRepository.followUser(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { FollowUserUseCaseResult.Success }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> FollowUserUseCaseResult.NetworkError(this)
    is NotFoundError -> FollowUserUseCaseResult.NotFoundError(this)
    else -> FollowUserUseCaseResult.GenericError(this)
}

sealed interface FollowUserUseCaseResult {
    object Success: FollowUserUseCaseResult
    data class  NetworkError(val error: DomainError): FollowUserUseCaseResult
    data class  NotFoundError(val error: DomainError): FollowUserUseCaseResult
    data class  GenericError(val error: DomainError): FollowUserUseCaseResult
}