package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface GetUsersUseCase {
    suspend operator fun invoke(username: String?): GetUsersUseCaseResult
}

class GetUsersUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : GetUsersUseCase {

    override suspend fun invoke(username: String?): GetUsersUseCaseResult =
        userProfileRepository.getUsers(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetUsersUseCaseResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetUsersUseCaseResult.NetworkError(this)
    else -> GetUsersUseCaseResult.GenericError(this)
}

sealed interface GetUsersUseCaseResult {
    data class Success(val users: List<ShortUserDomain>) : GetUsersUseCaseResult
    data class NetworkError(val error: DomainError) : GetUsersUseCaseResult
    data class GenericError(val error: DomainError) : GetUsersUseCaseResult
}