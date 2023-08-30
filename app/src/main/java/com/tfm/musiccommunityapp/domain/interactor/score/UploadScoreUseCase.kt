package com.tfm.musiccommunityapp.domain.interactor.score

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.ScoreRepository
import java.io.File

interface UploadScoreUseCase {
    suspend operator fun invoke(score: File): UploadScoreResult
}

class UploadScoreUseCaseImpl(
    private val scoreRepository: ScoreRepository
): UploadScoreUseCase {
    override suspend operator fun invoke(score: File): UploadScoreResult =
        scoreRepository.uploadScore(score).fold(
            { it.toErrorResult() },
            { UploadScoreResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> UploadScoreResult.NetworkError(this)
    else -> UploadScoreResult.GenericError(this)
}

sealed interface UploadScoreResult {
    data class Success(val scoreId: Long): UploadScoreResult
    data class NetworkError(val error: DomainError): UploadScoreResult
    data class GenericError(val error: DomainError): UploadScoreResult
}