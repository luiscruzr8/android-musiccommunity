package com.tfm.musiccommunityapp.usecase.comment

import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository

interface GetPostCommentsUseCase {
    suspend operator fun invoke(postId: Long) : GetPostCommentsResult
}

class GetPostCommentsUseCaseImpl(
    private val commentRepository: CommonPostRepository
) : GetPostCommentsUseCase {

    override suspend operator fun invoke(postId: Long) : GetPostCommentsResult =
        commentRepository.getPostComments(postId).fold(
            { it.toErrorResult() },
            { GetPostCommentsResult.Success(it) }
        )

}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetPostCommentsResult.NetworkError(this)
    else -> GetPostCommentsResult.GenericError(this)
}

sealed interface GetPostCommentsResult {
    data class Success(val comments: List<CommentDomain>) : GetPostCommentsResult
    data class NetworkError(val error: DomainError) : GetPostCommentsResult
    data class GenericError(val error: DomainError) : GetPostCommentsResult
}