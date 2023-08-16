package com.tfm.musiccommunityapp.domain.interactor.comment

import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.CommonPostRepository

interface PostOrRespondCommentUseCase {
    suspend operator fun invoke(postId: Long, commentId: Long?, comment: CommentDomain) : PostOrRespondCommentResult
}

class PostOrRespondCommentUseCaseImpl(
    private val commentRepository: CommonPostRepository
): PostOrRespondCommentUseCase {
    override suspend fun invoke(postId: Long, commentId: Long?, comment: CommentDomain): PostOrRespondCommentResult =
        commentRepository.postComment(postId, commentId, comment).fold(
            { it.toErrorResult() },
            { PostOrRespondCommentResult.Success }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> PostOrRespondCommentResult.NetworkError(this)
    else -> PostOrRespondCommentResult.GenericError(this)
}

sealed interface PostOrRespondCommentResult {
    object Success: PostOrRespondCommentResult
    data class NetworkError(val error: DomainError) : PostOrRespondCommentResult
    data class GenericError(val error: DomainError) : PostOrRespondCommentResult
}