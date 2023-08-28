package com.tfm.musiccommunityapp.data.datasource

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.CityDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

interface CityDatasource {

    suspend fun getCities(keyword: String?): Either<DomainError, List<CityDomain>>

    suspend fun getClosestCitiesByCoordinates(
        latitude: Double,
        longitude: Double
    ): Either<DomainError, List<CityDomain>>
}