package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.RecommendationDatasource
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain
import com.tfm.musiccommunityapp.domain.repository.RecommendationRepository

internal class RecommendationRepositoryImpl(
    private val recommendationDatasource: RecommendationDatasource
) : RecommendationRepository {
    override suspend fun getAllRecommendations(
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>> =
        recommendationDatasource.getAllRecommendations(top10, keyword)

    override suspend fun getUserRecommendations(
        login: String,
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>> =
        recommendationDatasource.getUserRecommendations(login, top10, keyword)

    override suspend fun getRecommendationById(id: Long): Either<DomainError, RecommendationDomain?> =
        recommendationDatasource.getRecommendationById(id)

    override suspend fun createRecommendation(recommendation: RecommendationDomain): Either<DomainError, Long> =
        recommendationDatasource.createRecommendation(recommendation)

    override suspend fun updateRecommendation(id: Long, recommendation: RecommendationDomain): Either<DomainError, Long> =
        recommendationDatasource.updateRecommendation(id, recommendation)

    override suspend fun deleteRecommendation(id: Long): Either<DomainError, Unit> =
        recommendationDatasource.deleteRecommendation(id)

    override suspend fun rateRecommendation(id: Long, rating: Int): Either<DomainError, Long> =
        recommendationDatasource.rateRecommendation(id, rating)
}