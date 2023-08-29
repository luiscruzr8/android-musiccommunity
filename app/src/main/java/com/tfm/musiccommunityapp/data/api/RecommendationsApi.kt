package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.RecommendationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

internal interface RecommendationsApi {

    companion object {
        const val API_RECOMMENDATIONS_URL = BuildConfig.BACKEND_URL + "/recommendations"
    }

    @GET(API_RECOMMENDATIONS_URL)
    suspend fun getRecommendations(
        @Query("top10") top10: Boolean,
        @Query("keyword") keyword: String?,
    ): Response<List<RecommendationResponse>>

    @GET("${API_RECOMMENDATIONS_URL}/user")
    suspend fun getUserRecommendations(
        @Query("login") login: String,
        @Query("top10") top10: Boolean,
        @Query("keyword") keyword: String?,
    ): Response<List<RecommendationResponse>>

    @GET("${API_RECOMMENDATIONS_URL}/{id}")
    suspend fun getRecommendationById(
        @Query("id") id: Long,
    ): Response<RecommendationResponse>

    @POST(API_RECOMMENDATIONS_URL)
    suspend fun createRecommendation(
        @Body recommendation: RecommendationResponse
    ): Response<Long>

    @PUT("${API_RECOMMENDATIONS_URL}/{id}")
    suspend fun updateRecommendation(
        @Query("id") id: Long,
        @Body recommendation: RecommendationResponse
    ): Response<Long>

    @DELETE("${API_RECOMMENDATIONS_URL}/{id}")
    suspend fun deleteRecommendation(
        @Query("id") id: Long,
    ): Response<Unit>

    @POST("${API_RECOMMENDATIONS_URL}/{id}/rate")
    suspend fun rateRecommendation(
        @Query("id") id: Long,
        @Query("rate") rate: Int,
    ): Response<Long>


}