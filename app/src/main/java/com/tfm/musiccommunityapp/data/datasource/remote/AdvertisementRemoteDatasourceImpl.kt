package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.api.PostsApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.api.model.toRequest
import com.tfm.musiccommunityapp.data.datasource.AdvertisementDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

internal class AdvertisementRemoteDatasourceImpl(
    private val advertisementApi: PostsApi
): AdvertisementDatasource {
    override suspend fun getAllAdvertisements(keyword: String?): Either<DomainError, List<AdvertisementDomain>> = eitherOf(
        request = { advertisementApi.getAdvertisements(keyword) },
        mapper = { it?.map { advertisement -> advertisement.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getAdvertisementById(advertisementId: Long): Either<DomainError, AdvertisementDomain?> = eitherOf(
        request = { advertisementApi.getAdvertisementById(advertisementId) },
        mapper = { it?.toDomain() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun createAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long> = eitherOf(
        request = { advertisementApi.createAdvertisement(advertisement.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun updateAdvertisement(advertisement: AdvertisementDomain): Either<DomainError, Long> = eitherOf(
        request = { advertisementApi.updateAdvertisement(advertisement.id, advertisement.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun deleteAdvertisement(advertisementId: Long): Either<DomainError, Unit> = eitherOf(
        request = { advertisementApi.deleteAdvertisementById(advertisementId) },
        mapper = { }
    ) { error -> error.toErrorResponse().toDomain() }
}
