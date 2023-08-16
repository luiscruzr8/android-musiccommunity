package com.tfm.musiccommunityapp.domain.interactor.opinion

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.OpinionRepository

interface DeleteOpinionUseCase {
    suspend operator fun invoke(opinionId: Long) : DeleteOpinionResult
}

class DeleteOpinionUseCaseImpl(
    private val opinionRepository: OpinionRepository
) : DeleteOpinionUseCase {
    override suspend operator fun invoke(opinionId: Long) : DeleteOpinionResult =
        opinionRepository.deleteOpinion(opinionId).fold(
            { it.toErrorResult() },
            { DeleteOpinionResult.Success }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> DeleteOpinionResult.NetworkError(this)
    else -> DeleteOpinionResult.GenericError(this)
}

sealed interface DeleteOpinionResult {
    object Success : DeleteOpinionResult
    data class NetworkError(val error: DomainError) : DeleteOpinionResult
    data class GenericError(val error: DomainError) : DeleteOpinionResult
}