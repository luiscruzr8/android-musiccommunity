package com.tfm.musiccommunityapp.usecase.score

import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.usecase.repository.ScoreRepository

interface GetScoreInfoByIdUseCase {
    suspend operator fun invoke(scoreId: Long): GetScoreInfoByIdResult
}

class GetScoreInfoByIdUseCaseImpl(
    private val scoreRepository: ScoreRepository
): GetScoreInfoByIdUseCase {
    override suspend operator fun invoke(scoreId: Long): GetScoreInfoByIdResult =
        scoreRepository.getScoreInfo(scoreId).fold(
            { toErrorResult() },
            { GetScoreInfoByIdResult.Success(it) }
        )
}

private fun toErrorResult() = GetScoreInfoByIdResult.NoScore

sealed interface GetScoreInfoByIdResult {
    data class Success(val score: ScoreDomain?): GetScoreInfoByIdResult
    object NoScore : GetScoreInfoByIdResult
}