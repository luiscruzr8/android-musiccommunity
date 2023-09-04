package com.tfm.musiccommunityapp.usecase.discussion

import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.DiscussionRepository

interface GetDiscussionsUseCase {
    suspend operator fun invoke(keyword: String?) : GetDiscussionsResult
}

class GetDiscussionsUseCaseImpl(
    private val discussionRepository: DiscussionRepository
) : GetDiscussionsUseCase {
    override suspend fun invoke(keyword: String?) : GetDiscussionsResult =
        discussionRepository.getDiscussions(keyword).fold(
            { it.toErrorResult() },
            { GetDiscussionsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetDiscussionsResult.NetworkError(this)
    else -> GetDiscussionsResult.GenericError(this)
}

sealed interface GetDiscussionsResult {
    data class Success(val discussions: List<DiscussionDomain>) : GetDiscussionsResult
    data class NetworkError(val error: DomainError) : GetDiscussionsResult
    data class GenericError(val error: DomainError) : GetDiscussionsResult
}