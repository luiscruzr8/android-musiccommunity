package com.tfm.musiccommunityapp.usecase.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.usecase.repository.UserProfileRepository

interface GetFollowingUseCase {
    suspend operator fun invoke(): GetFollowingUseCaseResult
}

class GetFollowingUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : GetFollowingUseCase {

    override suspend fun invoke(): GetFollowingUseCaseResult =
        userProfileRepository.getUserFollowing().fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetFollowingUseCaseResult.Success(it) }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetFollowingUseCaseResult.NetworkError(this)
    else -> GetFollowingUseCaseResult.GenericError(this)
}


sealed interface GetFollowingUseCaseResult {
    data class Success(val following: List<ShortUserDomain>) : GetFollowingUseCaseResult
    data class NetworkError(val error: DomainError): GetFollowingUseCaseResult
    data class GenericError(val error: DomainError): GetFollowingUseCaseResult
}

