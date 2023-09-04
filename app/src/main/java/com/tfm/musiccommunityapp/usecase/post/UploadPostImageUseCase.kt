package com.tfm.musiccommunityapp.usecase.post

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.NotFoundError
import com.tfm.musiccommunityapp.domain.model.ValidationError
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository
import java.io.File

interface UploadPostImageUseCase {
    suspend operator fun invoke(postId: Long, image: File): UploadPostImageResult
}

class UploadPostImageUseCaseImpl(
    private val commonPostRepository: CommonPostRepository
) : UploadPostImageUseCase {
    override suspend fun invoke(postId: Long, image: File): UploadPostImageResult =
        commonPostRepository.uploadPostImage(postId, image).fold(
            { it.toErrorResult() },
            { UploadPostImageResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NotFoundError -> UploadPostImageResult.NotFoundError(this)
    is ValidationError -> UploadPostImageResult.ValidationError(this)
    is NetworkError -> UploadPostImageResult.NetworkError(this)
    else -> UploadPostImageResult.GenericError(this)
}

sealed interface UploadPostImageResult {
    data class Success(val imageId: Long) : UploadPostImageResult
    data class NotFoundError(val error: DomainError) : UploadPostImageResult
    data class ValidationError(val error: DomainError) : UploadPostImageResult
    data class NetworkError(val error: DomainError) : UploadPostImageResult
    data class GenericError(val error: DomainError) : UploadPostImageResult
}