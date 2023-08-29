package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

interface CreateRecommendationUseCase {
    suspend operator fun invoke(recommendation: RecommendationDomain): CreateRecommendationResult
}

internal class CreateRecommendationUseCaseImpl(
    private val recommendationsRepository: RecommendationRepository
): CreateRecommendationUseCase {
    override suspend operator fun invoke(recommendation: RecommendationDomain): CreateRecommendationResult =
        recommendationsRepository.createRecommendation(recommendation).fold(
            { it.toErrorResult() },
            { CreateRecommendationResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> CreateRecommendationResult.NetworkError(this)
    else -> CreateRecommendationResult.GenericError(this)
}

sealed interface CreateRecommendationResult {
    data class Success(val id: Long): CreateRecommendationResult
    data class NetworkError(val error: DomainError): CreateRecommendationResult
    data class GenericError(val error: DomainError): CreateRecommendationResult
}