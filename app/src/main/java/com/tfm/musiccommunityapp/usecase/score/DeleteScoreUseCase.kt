package com.tfm.musiccommunityapp.usecase.score

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.ScoreRepository

interface DeleteScoreUseCase {
    suspend operator fun invoke(scoreId: Long): DeleteScoreResult
}

class DeleteScoreUseCaseImpl(
        private val scoreRepository: ScoreRepository
): DeleteScoreUseCase {
    override suspend operator fun invoke(scoreId: Long): DeleteScoreResult =
        scoreRepository.deleteScore(scoreId).fold(
            { it.toErrorResult() },
            { DeleteScoreResult.Success }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> DeleteScoreResult.NetworkError(this)
    else -> DeleteScoreResult.GenericError(this)
}

sealed interface DeleteScoreResult {
    object Success: DeleteScoreResult
    data class NetworkError(val error: DomainError): DeleteScoreResult
    data class GenericError(val error: DomainError): DeleteScoreResult
}