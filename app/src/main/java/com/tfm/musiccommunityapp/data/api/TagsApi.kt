package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.FollowerResponse
import com.tfm.musiccommunityapp.data.api.model.GenericPostResponse
import com.tfm.musiccommunityapp.data.api.model.TagResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface TagsApi {

    companion object {
        const val API_TAGS_URL = BuildConfig.BACKEND_URL + "tags"
    }

    @GET(API_TAGS_URL)
    suspend fun getAllTags(): Response<List<TagResponse>>

    @GET("${API_TAGS_URL}/users")
    suspend fun getUsersByTag(@Query("tagName") tagName: String): Response<List<FollowerResponse>>

    @GET("${API_TAGS_URL}/posts")
    suspend fun getPostsByTag(@Query("tagName") tagName: String): Response<List<GenericPostResponse>>

}