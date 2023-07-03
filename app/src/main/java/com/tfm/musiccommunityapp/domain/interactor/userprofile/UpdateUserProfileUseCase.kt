package com.tfm.musiccommunityapp.domain.interactor.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.domain.repository.UserProfileRepository

interface UpdateUserProfileUseCase {
    suspend operator fun invoke(
        user: UserDomain
    ): UpdateUserProfileUseCaseResult
}

class UpdateUserProfileUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : UpdateUserProfileUseCase {

    override suspend fun invoke(user: UserDomain): UpdateUserProfileUseCaseResult =
        userProfileRepository.updateUserInfo(user).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { UpdateUserProfileUseCaseResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UpdateUserProfileUseCaseResult.NetworkError(this)
    is NotFoundError -> UpdateUserProfileUseCaseResult.NotFoundError(this)
    else -> UpdateUserProfileUseCaseResult.GenericError(this)
}

sealed interface UpdateUserProfileUseCaseResult {
    data class Success(val user: UserDomain?): UpdateUserProfileUseCaseResult
    data class NetworkError(val error: DomainError): UpdateUserProfileUseCaseResult
    data class NotFoundError(val error: DomainError) : UpdateUserProfileUseCaseResult
    data class GenericError(val error: DomainError): UpdateUserProfileUseCaseResult
}