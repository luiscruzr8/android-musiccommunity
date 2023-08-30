package com.tfm.musiccommunityapp.domain.interactor.score

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.ScoreRepository
import java.io.File

interface GetScoreFileUseCase {
    suspend operator fun invoke(scoreId: Long): GetScoreFileResult
}

class GetScoreFileUseCaseImpl(
        private val scoreRepository: ScoreRepository
): GetScoreFileUseCase {
    override suspend operator fun invoke(scoreId: Long): GetScoreFileResult =
        scoreRepository.getScoreFile(scoreId).fold(
            { it.toErrorResult() },
            { GetScoreFileResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetScoreFileResult.NetworkError(this)
    else -> GetScoreFileResult.GenericError(this)
}

sealed interface GetScoreFileResult {
    data class Success(val scoreFile: File?): GetScoreFileResult
    data class NetworkError(val error: DomainError): GetScoreFileResult
    data class GenericError(val error: DomainError): GetScoreFileResult
}