package com.tfm.musiccommunityapp.domain.interactor.recommendations

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository


interface GetRecommendationByIdUseCase {
    suspend operator fun invoke(id: Long): GetRecommendationByIdResult
}

internal class GetRecommendationByIdUseCaseImpl(
        private val recommendationsRepository: RecommendationRepository
): GetRecommendationByIdUseCase {
    override suspend operator fun invoke(id: Long): GetRecommendationByIdResult =
        recommendationsRepository.getRecommendationById(id).fold(
            { it.toErrorResult() },
            { GetRecommendationByIdResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetRecommendationByIdResult.NetworkError(this)
    else -> GetRecommendationByIdResult.GenericError(this)
}

sealed interface GetRecommendationByIdResult {
    data class Success(val recommendation: RecommendationDomain?): GetRecommendationByIdResult
    data class NetworkError(val error: DomainError): GetRecommendationByIdResult
    data class GenericError(val error: DomainError): GetRecommendationByIdResult
}