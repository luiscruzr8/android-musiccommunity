package com.tfm.musiccommunityapp.usecase.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.RecommendationDomain

interface RecommendationRepository {

    suspend fun getAllRecommendations(
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>>

    suspend fun getUserRecommendations(
        login: String,
        top10: Boolean,
        keyword: String?
    ): Either<DomainError, List<RecommendationDomain>>

    suspend fun getRecommendationById(id: Long): Either<DomainError, RecommendationDomain?>

    suspend fun createRecommendation(recommendation: RecommendationDomain): Either<DomainError, Long>

    suspend fun updateRecommendation(id: Long, recommendation: RecommendationDomain): Either<DomainError, Long>

    suspend fun deleteRecommendation(id: Long): Either<DomainError, Unit>

    suspend fun rateRecommendation(id: Long, rating: Int): Either<DomainError, Long>
}