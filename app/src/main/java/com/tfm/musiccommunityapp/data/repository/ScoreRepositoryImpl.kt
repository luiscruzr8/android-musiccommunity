package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.ScoreDatasource
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.usecase.repository.ScoreRepository
import okhttp3.MultipartBody
import java.io.File

internal class ScoreRepositoryImpl(
    private val scoreDatasource: ScoreDatasource
) : ScoreRepository {

    override suspend fun getUserScores(
        login: String?,
        keyword: String?,
        onlyPublic: Boolean
    ): Either<DomainError, List<ScoreDomain>> =
        scoreDatasource.getUserScores(login, keyword, onlyPublic)

    override suspend fun uploadScore(score: File): Either<DomainError, Long> =
        scoreDatasource.uploadScore(score)

    override suspend fun getScoreInfo(scoreId: Long): Either<DomainError, ScoreDomain?> =
        scoreDatasource.getScoreInfo(scoreId)

    override suspend fun getScoreFile(scoreId: Long): Either<DomainError, File?> =
        scoreDatasource.getScoreFile(scoreId)

    override suspend fun deleteScore(scoreId: Long): Either<DomainError, Unit> =
        scoreDatasource.deleteScore(scoreId)

}