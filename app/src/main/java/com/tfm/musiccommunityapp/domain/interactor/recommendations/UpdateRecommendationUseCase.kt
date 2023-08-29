package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

interface UpdateRecommendationUseCase {
    suspend operator fun invoke(id: Long, recommendation: RecommendationDomain): UpdateRecommendationResult
}

internal class UpdateRecommendationUseCaseImpl(
    private val recommendationsRepository: RecommendationRepository
): UpdateRecommendationUseCase {
    override suspend operator fun invoke(id: Long, recommendation: RecommendationDomain): UpdateRecommendationResult =
        recommendationsRepository.updateRecommendation(id, recommendation).fold(
            { it.toErrorResult() },
            { UpdateRecommendationResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> UpdateRecommendationResult.NetworkError(this)
    else -> UpdateRecommendationResult.GenericError(this)
}

sealed interface UpdateRecommendationResult {
    data class Success(val id: Long): UpdateRecommendationResult
    data class NetworkError(val error: DomainError): UpdateRecommendationResult
    data class GenericError(val error: DomainError): UpdateRecommendationResult
}