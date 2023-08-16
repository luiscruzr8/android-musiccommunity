package com.tfm.musiccommunityapp.domain.interactor.advertisement

import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.AdvertisementRepository

interface UpdateAdvertisementUseCase {
    suspend operator fun invoke(updateAdvertisement: AdvertisementDomain) : UpdateAdvertisementResult
}

class UpdateAdvertisementUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : UpdateAdvertisementUseCase {
    override suspend operator fun invoke(updateAdvertisement: AdvertisementDomain) : UpdateAdvertisementResult =
        advertisementRepository.updateAdvertisement(updateAdvertisement).fold(
            { it.toErrorResult() },
            { UpdateAdvertisementResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> UpdateAdvertisementResult.NetworkError(this)
    else -> UpdateAdvertisementResult.GenericError(this)
}

sealed interface UpdateAdvertisementResult {
    data class Success(val id: Long) : UpdateAdvertisementResult
    data class NetworkError(val error: DomainError) : UpdateAdvertisementResult
    data class GenericError(val error: DomainError) : UpdateAdvertisementResult
}