package com.tfm.musiccommunityapp.domain.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

interface  AdvertisementRepository {

    suspend fun getAdvertisements(keyword: String?) : Either<DomainError, List<AdvertisementDomain>>

    suspend fun getAdvertisementById(advertisementId: Long): Either<DomainError, AdvertisementDomain?>

    suspend fun createAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long>

    suspend fun updateAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long>

    suspend fun deleteAdvertisement(advertisementId: Long): Either<DomainError, Unit>

}