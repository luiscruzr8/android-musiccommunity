package com.tfm.musiccommunityapp.domain.interactor.search

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.repository.TagRepository

interface GetUsersByTagUseCase {
    suspend operator fun invoke(tagName: String): GetUsersByTagUseCaseResult
}

class GetUsersByTagUseCaseImpl(
    private val tagRepository: TagRepository
) : GetUsersByTagUseCase {

    override suspend fun invoke(tagName: String): GetUsersByTagUseCaseResult =
        tagRepository.getUsersByTag(tagName).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetUsersByTagUseCaseResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetUsersByTagUseCaseResult.NetworkError(this)
    else -> GetUsersByTagUseCaseResult.GenericError(this)
}

sealed interface GetUsersByTagUseCaseResult {
    data class Success(val users: List<ShortUserDomain>) : GetUsersByTagUseCaseResult
    data class NetworkError(val error: DomainError) : GetUsersByTagUseCaseResult
    data class GenericError(val error: DomainError) : GetUsersByTagUseCaseResult
}