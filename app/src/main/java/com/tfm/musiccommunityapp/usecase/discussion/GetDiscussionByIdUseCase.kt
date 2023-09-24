package com.tfm.musiccommunityapp.usecase.discussion

import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.usecase.repository.DiscussionRepository

interface GetDiscussionByIdUseCase {
    suspend operator fun invoke(discussionId: Long) : GetDiscussionByIdResult
}

class GetDiscussionByIdUseCaseImpl(
    private val discussionRepository: DiscussionRepository
) : GetDiscussionByIdUseCase {
    override suspend fun invoke(discussionId: Long) : GetDiscussionByIdResult =
        discussionRepository.getDiscussionById(discussionId).fold(
            { toErrorResult() },
            { GetDiscussionByIdResult.Success(it) }
        )
}

private fun toErrorResult() = GetDiscussionByIdResult.NoDiscussion

sealed interface GetDiscussionByIdResult {
    data class Success(val discussion: DiscussionDomain?) : GetDiscussionByIdResult
    object NoDiscussion : GetDiscussionByIdResult
}