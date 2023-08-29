package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.RecommendationsApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.api.model.toResponse
import com.tfm.musiccommunityapp.data.datasource.RecommendationDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain

internal class RecommendationRemoteDatasourceImpl(
    private val recommendationApi: RecommendationsApi
) : RecommendationDatasource {
    override suspend fun getAllRecommendations(
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>> = eitherOf(
        response = recommendationApi.getRecommendations(top10, keyword),
        mapper = { it?.map { recommendationResponse -> recommendationResponse.toDomain() } ?: emptyList() }
    ) { it.toErrorResponse().toDomain() }

    override suspend fun getUserRecommendations(
        login: String,
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>> = eitherOf(
        response = recommendationApi.getUserRecommendations(login, top10, keyword),
        mapper = { it?.map { recommendationResponse -> recommendationResponse.toDomain() } ?: emptyList() }
    ) { it.toErrorResponse().toDomain() }

    override suspend fun getRecommendationById(id: Long): Either<DomainError, RecommendationDomain?> =
        eitherOf(
            response = recommendationApi.getRecommendationById(id),
            mapper = { it?.toDomain() }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun createRecommendation(recommendation: RecommendationDomain): Either<DomainError, Long> =
        eitherOf(
            response = recommendationApi.createRecommendation(recommendation.toResponse()),
            mapper = { it ?: -1L }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun updateRecommendation(id: Long, recommendation: RecommendationDomain): Either<DomainError, Long> =
        eitherOf(
            response = recommendationApi.updateRecommendation(id, recommendation.toResponse()),
            mapper = { it ?: -1L }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun deleteRecommendation(id: Long): Either<DomainError, Unit> = eitherOf(
        response = recommendationApi.deleteRecommendation(id),
        mapper = { }
    ) { it.toErrorResponse().toDomain() }

    override suspend fun rateRecommendation(id: Long, rating: Int): Either<DomainError, Long> =
        eitherOf(
            response = recommendationApi.rateRecommendation(id, rating),
            mapper = { it ?: -1L }
        ) { it.toErrorResponse().toDomain() }
}