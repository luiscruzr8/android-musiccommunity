package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.AdvertisementDatasource
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.repository.AdvertisementRepository

class AdvertisementRepositoryImpl(
    private val advertisementDatasource: AdvertisementDatasource
) : AdvertisementRepository {
    override suspend fun getAdvertisements(keyword: String?): Either<DomainError, List<AdvertisementDomain>> =
        getAdvertisements(keyword)

    override suspend fun getAdvertisementById(advertisementId: Long): Either<DomainError, AdvertisementDomain?> =
        getAdvertisementById(advertisementId)

    override suspend fun createAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long> =
        createAdvertisement(advertisement)

    override suspend fun updateAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long> =
        updateAdvertisement(advertisement)

    override suspend fun deleteAdvertisement(advertisementId: Long): Either<DomainError, Unit> =
        deleteAdvertisement(advertisementId)

}