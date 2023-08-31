package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.ScoresApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.datasource.ScoreDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class ScoreRemoteDatasourceImpl(
    private val scoresApi: ScoresApi
) : ScoreDatasource {

    override suspend fun getUserScores(
        login: String?,
        keyword: String?,
        onlyPublic: Boolean
    ): Either<DomainError, List<ScoreDomain>> = eitherOf(
        response = scoresApi.getUserScores(login, keyword, onlyPublic),
        mapper = { it?.map { it2 -> it2.toDomain() } ?: emptyList() }
    ) { it.toErrorResponse().toDomain() }

    override suspend fun uploadScore(score: File): Either<DomainError, Long> =
        eitherOf(
            request = {
                val requestFile = score.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val scoreBody = MultipartBody.Part.createFormData("score", score.name, requestFile)
                scoresApi.uploadScore(scoreBody)
            },
            mapper = { it ?: -1L }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun getScoreInfo(scoreId: Long): Either<DomainError, ScoreDomain?> =
        eitherOf(
            response = scoresApi.getScoreInfo(scoreId),
            mapper = { it?.toDomain() }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun getScoreFile(scoreId: Long): Either<DomainError, File?> =
        eitherOf(
            response = scoresApi.getScoreFile(scoreId),
            mapper = { it }
        ) { it.toErrorResponse().toDomain() }

    override suspend fun deleteScore(scoreId: Long): Either<DomainError, Unit> =
        eitherOf(
            response = scoresApi.deleteScore(scoreId),
            mapper = { }
        ) { it.toErrorResponse().toDomain() }
}