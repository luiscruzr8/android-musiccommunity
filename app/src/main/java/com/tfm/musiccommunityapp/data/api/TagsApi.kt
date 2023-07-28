package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.GenericPostResponse
import com.tfm.musiccommunityapp.data.api.model.TagResponse
import com.tfm.musiccommunityapp.data.api.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface TagsApi {

    companion object {
        const val API_TAGS_URL = BuildConfig.BACKEND_URL + "tags"
    }

    @GET(API_TAGS_URL)
    suspend fun getAllTags(): Response<List<TagResponse>>

    @GET("${API_TAGS_URL}/users")
    suspend fun getUsersByTag(tagName: String): Response<List<UserResponse>>

    @GET("${API_TAGS_URL}/posts")
    suspend fun getPostsByTag(tagName: String): Response<List<GenericPostResponse>>

}