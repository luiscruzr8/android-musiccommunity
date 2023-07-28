package com.tfm.musiccommunityapp.domain.interactor.tags

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.TagDomain
import com.tfm.musiccommunityapp.domain.repository.TagRepository

interface GetTagsUseCase {
    suspend operator fun invoke(): GetTagsUseCaseResult
}

class GetTagsUseCaseImpl(
    private val tagRepository: TagRepository
) : GetTagsUseCase {

    override suspend fun invoke(): GetTagsUseCaseResult =
        tagRepository.getAllTags().fold(
            ifLeft = { it.toErrorResult() },
            ifRight = { GetTagsUseCaseResult.Success(it) }
        )
}


private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetTagsUseCaseResult.NetworkError(this)
    else -> GetTagsUseCaseResult.GenericError(this)
}

sealed interface GetTagsUseCaseResult {
    data class Success(val tags: List<TagDomain>) : GetTagsUseCaseResult
    data class NetworkError(val error: DomainError) : GetTagsUseCaseResult
    data class GenericError(val error: DomainError) : GetTagsUseCaseResult
}