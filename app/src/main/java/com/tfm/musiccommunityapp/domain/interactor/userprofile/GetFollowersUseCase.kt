package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface GetFollowersUseCase {
    suspend operator fun invoke(username: String?): GetFollowersUseCaseResult
}

class GetFollowersUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : GetFollowersUseCase {

    override suspend fun invoke(username: String?): GetFollowersUseCaseResult =
        userProfileRepository.getUserFollowers(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetFollowersUseCaseResult.Success(it) }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetFollowersUseCaseResult.NetworkError(this)
    is NotFoundError -> GetFollowersUseCaseResult.NotFoundError(this)
    else -> GetFollowersUseCaseResult.GenericError(this)
}


sealed interface GetFollowersUseCaseResult {
    data class Success(val followers: List<ShortUserDomain>) : GetFollowersUseCaseResult
    data class NetworkError(val error: DomainError): GetFollowersUseCaseResult
    data class NotFoundError(val error: DomainError): GetFollowersUseCaseResult
    data class GenericError(val error: DomainError): GetFollowersUseCaseResult
}

