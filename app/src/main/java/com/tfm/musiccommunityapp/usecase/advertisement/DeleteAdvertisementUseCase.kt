package com.tfm.musiccommunityapp.usecase.advertisement

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.AdvertisementRepository

interface DeleteAdvertisementUseCase {
    suspend operator fun invoke(advertisementId: Long) : DeleteAdvertisementResult
}

class DeleteAdvertisementUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : DeleteAdvertisementUseCase {
    override suspend operator fun invoke(advertisementId: Long) : DeleteAdvertisementResult =
        advertisementRepository.deleteAdvertisement(advertisementId).fold(
            { it.toErrorResult() },
            { DeleteAdvertisementResult.Success }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> DeleteAdvertisementResult.NetworkError(this)
    else -> DeleteAdvertisementResult.GenericError(this)
}

sealed interface DeleteAdvertisementResult {
    object Success : DeleteAdvertisementResult
    data class NetworkError(val error: DomainError) : DeleteAdvertisementResult
    data class GenericError(val error: DomainError) : DeleteAdvertisementResult
}