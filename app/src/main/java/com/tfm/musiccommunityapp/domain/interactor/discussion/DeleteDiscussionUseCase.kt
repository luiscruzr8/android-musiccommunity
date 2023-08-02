package com.tfm.musiccommunityapp.domain.interactor.discussion

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.DiscussionRepository

interface DeleteDiscussionUseCase {
    suspend operator fun invoke(discussionId: Long) : DeleteDiscussionResult
}

class DeleteDiscussionUseCaseImpl(
    private val discussionRepository: DiscussionRepository
) : DeleteDiscussionUseCase {
    override suspend operator fun invoke(discussionId: Long) : DeleteDiscussionResult =
        discussionRepository.deleteDiscussion(discussionId).fold(
            { it.toErrorResult() },
            { DeleteDiscussionResult.Success }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> DeleteDiscussionResult.NetworkError(this)
    else -> DeleteDiscussionResult.GenericError(this)
}

sealed interface DeleteDiscussionResult {
    object Success : DeleteDiscussionResult
    data class NetworkError(val error: DomainError) : DeleteDiscussionResult
    data class GenericError(val error: DomainError) : DeleteDiscussionResult
}