package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

interface GetRecommendationsUseCase {
    suspend operator fun invoke(top10: Boolean, keyword: String?): GetRecommendationsResult
}

internal class GetRecommendationsUseCaseImpl(
    private val recommendationsRepository: RecommendationRepository
): GetRecommendationsUseCase {
    override suspend operator fun invoke(top10: Boolean, keyword: String?): GetRecommendationsResult =
        recommendationsRepository.getAllRecommendations(top10, keyword).fold(
            { it.toErrorResult() },
            { GetRecommendationsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetRecommendationsResult.NetworkError(this)
    else -> GetRecommendationsResult.GenericError(this)
}

sealed interface GetRecommendationsResult {
    data class Success(val recommendations: List<RecommendationDomain>): GetRecommendationsResult
    data class NetworkError(val error: DomainError): GetRecommendationsResult
    data class GenericError(val error: DomainError): GetRecommendationsResult
}