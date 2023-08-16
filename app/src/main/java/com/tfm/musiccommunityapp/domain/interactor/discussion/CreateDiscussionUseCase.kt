package com.tfm.musiccommunityapp.domain.interactor.discussion

import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.DiscussionRepository

interface CreateDiscussionUseCase {
    suspend operator fun invoke(newDiscussion: DiscussionDomain) : CreateDiscussionResult
}

class CreateDiscussionUseCaseImpl(
    private val discussionRepository: DiscussionRepository
) : CreateDiscussionUseCase {
    override suspend operator fun invoke(newDiscussion: DiscussionDomain) : CreateDiscussionResult =
        discussionRepository.createDiscussion(newDiscussion).fold(
            { it.toErrorResult() },
            { CreateDiscussionResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> CreateDiscussionResult.NetworkError(this)
    else -> CreateDiscussionResult.GenericError(this)
}

sealed interface CreateDiscussionResult {
    data class Success(val id: Long) : CreateDiscussionResult
    data class NetworkError(val error: DomainError) : CreateDiscussionResult
    data class GenericError(val error: DomainError) : CreateDiscussionResult
}