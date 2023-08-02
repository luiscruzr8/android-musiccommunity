package com.tfm.musiccommunityapp.domain.interactor.advertisement

import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.domain.repository.AdvertisementRepository

interface CreateAdvertisementUseCase {
    suspend operator fun invoke(newAdvertisement: AdvertisementDomain) : CreateAdvertisementResult
}

class CreateAdvertisementUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : CreateAdvertisementUseCase {
    override suspend operator fun invoke(newAdvertisement: AdvertisementDomain) : CreateAdvertisementResult =
        advertisementRepository.createAdvertisement(newAdvertisement).fold(
            { it.toErrorResult() },
            { CreateAdvertisementResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> CreateAdvertisementResult.NetworkError(this)
    else -> CreateAdvertisementResult.GenericError(this)
}

sealed interface CreateAdvertisementResult {
    data class Success(val id: Long) : CreateAdvertisementResult
    data class NetworkError(val error: DomainError) : CreateAdvertisementResult
    data class GenericError(val error: DomainError) : CreateAdvertisementResult
}