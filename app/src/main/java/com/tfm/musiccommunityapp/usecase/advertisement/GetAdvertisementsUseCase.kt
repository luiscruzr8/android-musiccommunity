package com.tfm.musiccommunityapp.usecase.advertisement

import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.AdvertisementRepository

interface GetAdvertisementsUseCase {
    suspend operator fun invoke(keyword: String?) : GetAdvertisementsResult
}

class GetAdvertisementsUseCaseImpl(
    private val advertisementRepository: AdvertisementRepository
) : GetAdvertisementsUseCase {
    override suspend fun invoke(keyword: String?) : GetAdvertisementsResult =
        advertisementRepository.getAdvertisements(keyword).fold(
            { it.toErrorResult() },
            { GetAdvertisementsResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> GetAdvertisementsResult.NetworkError(this)
    else -> GetAdvertisementsResult.GenericError(this)
}

sealed interface GetAdvertisementsResult {
    data class Success(val advertisements: List<AdvertisementDomain>) : GetAdvertisementsResult
    data class NetworkError(val error: DomainError) : GetAdvertisementsResult
    data class GenericError(val error: DomainError) : GetAdvertisementsResult
}