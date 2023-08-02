package com.tfm.musiccommunityapp.domain.interactor.opinion

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.repository.OpinionRepository

interface GetOpinionsUseCase {
    suspend operator fun invoke(keyword: String?) : GetOpinionsResult
}

class GetOpinionsUseCaseImpl(
    private val opinionRepository: OpinionRepository
) : GetOpinionsUseCase {
    override suspend fun invoke(keyword: String?) : GetOpinionsResult =
        opinionRepository.getOpinions(keyword).fold(
            { it.toErrorResult() },
            { GetOpinionsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetOpinionsResult.NetworkError(this)
    else -> GetOpinionsResult.GenericError(this)
}

sealed interface GetOpinionsResult {
    data class Success(val opinions: List<OpinionDomain>) : GetOpinionsResult
    data class NetworkError(val error: DomainError) : GetOpinionsResult
    data class GenericError(val error: DomainError) : GetOpinionsResult
}