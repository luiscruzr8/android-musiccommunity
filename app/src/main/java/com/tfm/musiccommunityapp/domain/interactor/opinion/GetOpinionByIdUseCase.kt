package com.tfm.musiccommunityapp.domain.interactor.opinion

import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.repository.OpinionRepository

interface GetOpinionByIdUseCase {
    suspend operator fun invoke(opinionId: Long) : GetOpinionByIdResult
}

class GetOpinionByIdUseCaseImpl(
    private val opinionRepository: OpinionRepository
) : GetOpinionByIdUseCase {
    override suspend fun invoke(opinionId: Long) : GetOpinionByIdResult =
        opinionRepository.getOpinionById(opinionId).fold(
            { toErrorResult() },
            { GetOpinionByIdResult.Success(it) }
        )
}

private fun toErrorResult() = GetOpinionByIdResult.NoOpinion

sealed interface GetOpinionByIdResult {
    data class Success(val opinion: OpinionDomain?) : GetOpinionByIdResult
    object NoOpinion : GetOpinionByIdResult
}