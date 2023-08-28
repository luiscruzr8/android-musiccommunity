package com.tfm.musiccommunityapp.data.api.model

import com.tfm.musiccommunityapp.domain.model.CityDomain

internal data class CityResponse(
    val id: Long,
    val cityName: String,
    val countryName: String
)

internal fun CityResponse.toDomain() = CityDomain(
    id = id,
    cityName = cityName,
    countryName = countryName
)