package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.ScoreResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

internal interface ScoresApi {

    companion object {
        const val API_SCORES_URL = BuildConfig.BACKEND_URL + "/scores"
    }

    @GET(API_SCORES_URL)
    suspend fun getUserScores(
        @Query("login") login: String?,
        @Query("keyword") keyword: String?,
        @Query("onlyPublic") onlyPublic: Boolean
    ): Response<List<ScoreResponse>>

    @Multipart
    @POST("${API_SCORES_URL}/uploadScore")
    suspend fun uploadScore(
        @Part score: MultipartBody.Part
    ): Response<Long>

    @GET("${API_SCORES_URL}/score-info/{scoreId}")
    suspend fun getScoreInfo(
        @Path("scoreId") scoreId: Long
    ): Response<ScoreResponse>

    @GET("${API_SCORES_URL}/score/{scoreId}")
    suspend fun getScoreFile(@Path("scoreId") scoreId: Long): Response<File>

    @DELETE("${API_SCORES_URL}/score/{scoreId}")
    suspend fun deleteScore(@Path("scoreId") scoreId: Long): Response<Unit>

}