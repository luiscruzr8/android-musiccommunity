package com.tfm.musiccommunityapp.usecase.advertisement

import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.usecase.repository.AdvertisementRepository

interface GetAdvertisementByIdUseCase {
    suspend operator fun invoke(advertisementId: Long) : GetAdvertisementByIdResult
}

class GetAdvertisementByIdUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : GetAdvertisementByIdUseCase {
    override suspend fun invoke(advertisementId: Long) : GetAdvertisementByIdResult =
        advertisementRepository.getAdvertisementById(advertisementId).fold(
            { toErrorResult() },
            { GetAdvertisementByIdResult.Success(it) }
        )
}

private fun toErrorResult() = GetAdvertisementByIdResult.NoAdvertisement

sealed interface GetAdvertisementByIdResult {
    data class Success(val advertisement: AdvertisementDomain?) : GetAdvertisementByIdResult
    object NoAdvertisement : GetAdvertisementByIdResult
}