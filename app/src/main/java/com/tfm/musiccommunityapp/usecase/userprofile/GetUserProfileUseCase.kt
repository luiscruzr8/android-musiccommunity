package com.tfm.musiccommunityapp.usecase.userprofile

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.UserDomain
import com.tfm.musiccommunityapp.usecase.repository.UserProfileRepository

interface GetUserProfileUseCase {
    suspend operator fun invoke(username:String?): GetUserProfileUseCaseResult
}

class GetUserProfileUseCaseImpl(
    private val userProfileRepository: UserProfileRepository
) : GetUserProfileUseCase {

    override suspend fun invoke(username:String?): GetUserProfileUseCaseResult =
        userProfileRepository.getUserInfo(username).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { it.let { GetUserProfileUseCaseResult.Success(it) } }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetUserProfileUseCaseResult.NetworkError(this)
    is NotFoundError -> GetUserProfileUseCaseResult.NotFoundError(this)
    else -> GetUserProfileUseCaseResult.GenericError(this)
}

sealed interface GetUserProfileUseCaseResult {
    data class Success(val user: UserDomain?) : GetUserProfileUseCaseResult
    data class NetworkError(val error: DomainError) : GetUserProfileUseCaseResult
    data class NotFoundError(val error: DomainError) : GetUserProfileUseCaseResult
    data class GenericError(val error: DomainError) : GetUserProfileUseCaseResult
}