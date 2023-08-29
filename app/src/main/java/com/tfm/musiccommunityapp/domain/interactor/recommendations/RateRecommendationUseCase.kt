package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

interface RateRecommendationUseCase {
    suspend operator fun invoke(id: Long, rating: Int): RateRecommendationResult
}

internal class RateRecommendationUseCaseImpl(
    private val recommendationsRepository: RecommendationRepository
): RateRecommendationUseCase {
    override suspend operator fun invoke(id: Long, rating: Int): RateRecommendationResult =
        recommendationsRepository.rateRecommendation(id, rating).fold(
            { it.toErrorResult() },
            { RateRecommendationResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> RateRecommendationResult.NetworkError(this)
    else -> RateRecommendationResult.GenericError(this)
}

sealed interface RateRecommendationResult {
    data class Success(val id: Long): RateRecommendationResult
    data class NetworkError(val error: DomainError): RateRecommendationResult
    data class GenericError(val error: DomainError): RateRecommendationResult
}