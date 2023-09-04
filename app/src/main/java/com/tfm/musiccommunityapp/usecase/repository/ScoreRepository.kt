package com.tfm.musiccommunityapp.usecase.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import java.io.File

interface ScoreRepository {

    suspend fun getUserScores(
        login: String?,
        keyword: String?,
        onlyPublic: Boolean
    ): Either<DomainError, List<ScoreDomain>>

    suspend fun uploadScore(score: File): Either<DomainError, Long>

    suspend fun getScoreInfo(scoreId: Long): Either<DomainError, ScoreDomain?>

    suspend fun getScoreFile(scoreId: Long): Either<DomainError, File?>

    suspend fun deleteScore(scoreId: Long): Either<DomainError, Unit>

}