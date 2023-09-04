package com.tfm.musiccommunityapp.usecase.search

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.TagRepository

interface GetPostsByTagUseCase {
    suspend operator fun invoke(tagName: String): GetPostsByTagUseCaseResult
}

class GetPostsByTagUseCaseImpl(
    private val tagRepository: TagRepository
) : GetPostsByTagUseCase {

    override suspend fun invoke(tagName: String): GetPostsByTagUseCaseResult =
        tagRepository.getPostsByTag(tagName).fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetPostsByTagUseCaseResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetPostsByTagUseCaseResult.NetworkError(this)
    else -> GetPostsByTagUseCaseResult.GenericError(this)
}

sealed interface GetPostsByTagUseCaseResult {
    data class Success(val posts: List<GenericPostDomain>) : GetPostsByTagUseCaseResult
    data class NetworkError(val error: DomainError) : GetPostsByTagUseCaseResult
    data class GenericError(val error: DomainError) : GetPostsByTagUseCaseResult
}