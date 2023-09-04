package com.tfm.musiccommunityapp.usecase.opinion

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.usecase.repository.OpinionRepository

interface UpdateOpinionUseCase {
    suspend operator fun invoke(updateOpinion: OpinionDomain) : UpdateOpinionResult
}

class UpdateOpinionUseCaseImpl(
    private val opinionRepository: OpinionRepository
) : UpdateOpinionUseCase {
    override suspend operator fun invoke(updateOpinion: OpinionDomain) : UpdateOpinionResult =
        opinionRepository.updateOpinion(updateOpinion).fold(
            { it.toErrorResult() },
            { UpdateOpinionResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UpdateOpinionResult.NetworkError(this)
    else -> UpdateOpinionResult.GenericError(this)
}

sealed interface UpdateOpinionResult {
    data class Success(val id: Long) : UpdateOpinionResult
    data class NetworkError(val error: DomainError) : UpdateOpinionResult
    data class GenericError(val error: DomainError) : UpdateOpinionResult
}