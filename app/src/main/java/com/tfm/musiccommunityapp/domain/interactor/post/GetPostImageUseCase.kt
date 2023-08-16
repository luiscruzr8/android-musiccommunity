package com.tfm.musiccommunityapp.domain.interactor.post

import com.tfm.musiccommunityapp.domain.repository.CommonPostRepository

interface GetPostImageUseCase {
    suspend operator fun invoke(postId: Long): GetPostImageResult
}

class GetPostImageUseCaseImpl(
    private val commonPostRepository: CommonPostRepository
) : GetPostImageUseCase {
    override suspend fun invoke(postId: Long): GetPostImageResult =
        commonPostRepository.getPostImage(postId).fold(
            { GetPostImageResult.Success("") },
            { GetPostImageResult.Success(it) }
        )
}

sealed interface GetPostImageResult {
    data class Success(val imageUrl: String) : GetPostImageResult
}