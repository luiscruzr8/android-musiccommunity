package com.tfm.musiccommunityapp.data.repository

import com.tfm.musiccommunityapp.data.datasource.CityDatasource
import com.tfm.musiccommunityapp.domain.repository.CityRepository

class CityRepositoryImpl(
        private val cityDatasource: CityDatasource
) : CityRepository {

    override suspend fun getCities(keyword: String?) =
        cityDatasource.getCities(keyword)

    override suspend fun getClosestCitiesByCoordinates(latitude: Double, longitude: Double) =
            cityDatasource.getClosestCitiesByCoordinates(latitude, longitude)

}