package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface IsUserFollowerOfUseCase {
    suspend operator fun invoke(username: String): IsUserFollowerOfUseCaseResult
}

class IsUserFollowerOfUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : IsUserFollowerOfUseCase {

    override suspend fun invoke(username: String): IsUserFollowerOfUseCaseResult =
        userProfileRepository.checkIfUserIsFollowerOf(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { it.let { IsUserFollowerOfUseCaseResult.Success(it ?: false) } }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> IsUserFollowerOfUseCaseResult.NetworkError(this)
    is NotFoundError -> IsUserFollowerOfUseCaseResult.NotFoundError(this)
    else -> IsUserFollowerOfUseCaseResult.GenericError(this)
}

sealed interface IsUserFollowerOfUseCaseResult {
    data class Success(val isFollower: Boolean): IsUserFollowerOfUseCaseResult
    data class NetworkError(val error: DomainError): IsUserFollowerOfUseCaseResult
    data class NotFoundError(val error: DomainError): IsUserFollowerOfUseCaseResult
    data class GenericError(val error: DomainError): IsUserFollowerOfUseCaseResult
}