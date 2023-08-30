package com.tfm.musiccommunityapp.domain.interactor.score

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.domain.repository.ScoreRepository

interface GetUserScoresUseCase {
    suspend operator fun invoke(
        login: String?,
        keyword: String?,
        onlyPublic: Boolean
    ): GetUserScoresResult
}

internal class GetUserScoresUseCaseImpl(
    private val scoreRepository: ScoreRepository
): GetUserScoresUseCase {
    override suspend operator fun invoke(
        login: String?,
        keyword: String?,
        onlyPublic: Boolean
    ): GetUserScoresResult = scoreRepository.getUserScores(login, keyword, onlyPublic)
        .fold(
            { it.toErrorResult() },
            { GetUserScoresResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when(this) {
    is NetworkError -> GetUserScoresResult.NetworkError(this)
    else -> GetUserScoresResult.GenericError(this)
}

sealed interface GetUserScoresResult {
    data class Success(val scores: List<ScoreDomain>): GetUserScoresResult
    data class NetworkError(val error: DomainError): GetUserScoresResult
    data class GenericError(val error: DomainError): GetUserScoresResult
}