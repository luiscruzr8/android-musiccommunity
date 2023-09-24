package com.tfm.musiccommunityapp.usecase.discussion

import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.DiscussionRepository

interface UpdateDiscussionUseCase {
    suspend operator fun invoke(updateDiscussion: DiscussionDomain) : UpdateDiscussionResult
}

class UpdateDiscussionUseCaseImpl(
    private val discussionRepository: DiscussionRepository
) : UpdateDiscussionUseCase {
    override suspend operator fun invoke(updateDiscussion: DiscussionDomain) : UpdateDiscussionResult =
        discussionRepository.updateDiscussion(updateDiscussion).fold(
            { it.toErrorResult() },
            { UpdateDiscussionResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UpdateDiscussionResult.NetworkError(this)
    else -> UpdateDiscussionResult.GenericError(this)
}

sealed interface UpdateDiscussionResult {
    data class Success(val id: Long) : UpdateDiscussionResult
    data class NetworkError(val error: DomainError) : UpdateDiscussionResult
    data class GenericError(val error: DomainError) : UpdateDiscussionResult
}