package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface GetUsersUseCase {
    suspend operator fun invoke(): GetUsersUseCaseResult
}

class GetUsersUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : GetUsersUseCase {

    override suspend fun invoke(): GetUsersUseCaseResult =
        userProfileRepository.getUsers().fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetUsersUseCaseResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetUsersUseCaseResult.NetworkError(this)
    else -> GetUsersUseCaseResult.GenericError(this)
}

sealed interface GetUsersUseCaseResult {
    data class Success(val users: List<UserDomain>) : GetUsersUseCaseResult
    data class NetworkError(val error: DomainError) : GetUsersUseCaseResult
    data class GenericError(val error: DomainError) : GetUsersUseCaseResult
}