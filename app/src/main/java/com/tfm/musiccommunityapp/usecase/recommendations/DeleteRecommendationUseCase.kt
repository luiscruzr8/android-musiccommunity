package com.tfm.musiccommunityapp.usecase.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.RecommendationRepository

interface DeleteRecommendationUseCase {
    suspend operator fun invoke(id: Long): DeleteRecommendationResult
}

internal class DeleteRecommendationUseCaseImpl(
    private val recommendationsRepository: RecommendationRepository
): DeleteRecommendationUseCase {
    override suspend operator fun invoke(id: Long): DeleteRecommendationResult =
        recommendationsRepository.deleteRecommendation(id).fold(
            { it.toErrorResult() },
            { DeleteRecommendationResult.Success }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> DeleteRecommendationResult.NetworkError(this)
    else -> DeleteRecommendationResult.GenericError(this)
}

sealed interface DeleteRecommendationResult {
    object Success: DeleteRecommendationResult
    data class NetworkError(val error: DomainError): DeleteRecommendationResult
    data class GenericError(val error: DomainError): DeleteRecommendationResult
}