package com.tfm.musiccommunityapp.usecase.comment

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository

interface DeleteCommentUseCase {
    suspend operator fun invoke(postId: Long, commentId: Long) : DeleteCommentResult
}

class DeleteCommentUseCaseImpl(
    private val commentRepository: CommonPostRepository
): DeleteCommentUseCase {
    override suspend fun invoke(postId: Long, commentId: Long): DeleteCommentResult =
        commentRepository.deleteComment(postId, commentId).fold(
            { it.toErrorResult() },
            { DeleteCommentResult.Success }
    )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> DeleteCommentResult.NetworkError(this)
    else -> DeleteCommentResult.GenericError(this)
}

sealed interface DeleteCommentResult {
    object Success: DeleteCommentResult
    data class NetworkError(val error: DomainError) : DeleteCommentResult
    data class GenericError(val error: DomainError) : DeleteCommentResult
}