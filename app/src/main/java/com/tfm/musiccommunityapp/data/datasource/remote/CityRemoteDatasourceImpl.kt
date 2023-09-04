package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.api.CitiesApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.data.datasource.CityDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

internal class CityRemoteDatasourceImpl(
        private val cityApi: CitiesApi
) : CityDatasource {

    override suspend fun getCities(keyword: String?): Either<DomainError, List<CityDomain>> = eitherOf(
            response = cityApi.getAllCities(keyword),
            mapper = { cities -> cities?.map { it.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getClosestCitiesByCoordinates(
        latitude: Double,
        longitude: Double
    ): Either<DomainError, List<CityDomain>> = eitherOf(
            response = cityApi.getClosestCities(latitude, longitude),
            mapper = { cities -> cities?.map { it.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }
}