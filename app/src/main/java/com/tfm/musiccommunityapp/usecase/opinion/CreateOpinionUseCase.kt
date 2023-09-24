package com.tfm.musiccommunityapp.usecase.opinion

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.usecase.repository.OpinionRepository

interface CreateOpinionUseCase {
    suspend operator fun invoke(newOpinion: OpinionDomain) : CreateOpinionResult
}

class CreateOpinionUseCaseImpl(
    private val opinionRepository: OpinionRepository
) : CreateOpinionUseCase {
    override suspend operator fun invoke(newOpinion: OpinionDomain) : CreateOpinionResult =
        opinionRepository.createOpinion(newOpinion).fold(
            { it.toErrorResult() },
            { CreateOpinionResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> CreateOpinionResult.NetworkError(this)
    else -> CreateOpinionResult.GenericError(this)
}

sealed interface CreateOpinionResult {
    data class Success(val id: Long) : CreateOpinionResult
    data class NetworkError(val error: DomainError) : CreateOpinionResult
    data class GenericError(val error: DomainError) : CreateOpinionResult
}