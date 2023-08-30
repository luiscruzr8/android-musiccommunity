package com.tfm.musiccommunityapp.domain.interactor.score

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.domain.repository.ScoreRepository

interface GetScoreInfoByIdUseCase {
    suspend operator fun invoke(scoreId: Long): GetScoreInfoByIdResult
}

class GetScoreInfoByIdUseCaseImpl(
    private val scoreRepository: ScoreRepository
): GetScoreInfoByIdUseCase {
    override suspend operator fun invoke(scoreId: Long): GetScoreInfoByIdResult =
        scoreRepository.getScoreInfo(scoreId).fold(
            { it.toErrorResult() },
            { GetScoreInfoByIdResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetScoreInfoByIdResult.NetworkError(this)
    else -> GetScoreInfoByIdResult.GenericError(this)
}

sealed interface GetScoreInfoByIdResult {
    data class Success(val score: ScoreDomain?): GetScoreInfoByIdResult
    data class NetworkError(val error: DomainError): GetScoreInfoByIdResult
    data class GenericError(val error: DomainError): GetScoreInfoByIdResult
}