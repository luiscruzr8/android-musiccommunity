package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

interface GetUserRecommendationsUseCase {
    suspend operator fun invoke(login: String, top10: Boolean, keyword: String?): GetUserRecommendationsResult
}

internal class GetUserRecommendationsUseCaseImpl(
        private val recommendationsRepository: RecommendationRepository
): GetUserRecommendationsUseCase {
    override suspend operator fun invoke(login: String, top10: Boolean, keyword: String?): GetUserRecommendationsResult =
        recommendationsRepository.getUserRecommendations(login, top10, keyword).fold(
            { it.toErrorResult() },
            { GetUserRecommendationsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetUserRecommendationsResult.NetworkError(this)
    else -> GetUserRecommendationsResult.GenericError(this)
}

sealed interface GetUserRecommendationsResult {
    data class Success(val recommendations: List<RecommendationDomain>): GetUserRecommendationsResult
    data class NetworkError(val error: DomainError): GetUserRecommendationsResult
    data class GenericError(val error: DomainError): GetUserRecommendationsResult
}