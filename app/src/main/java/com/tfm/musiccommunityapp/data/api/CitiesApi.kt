package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.CityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CitiesApi {

    companion object {
        const val API_CITIES_URL = BuildConfig.BACKEND_URL + "cities"
    }

    @GET(API_CITIES_URL)
    suspend fun getAllCities(
        @Query("keyword") keyword: String? = null
    ): Response<List<CityResponse>>

    @GET("${API_CITIES_URL}/closest")
    suspend fun getClosestCities(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<List<CityResponse>>
}