package com.tfm.musiccommunityapp.data.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.data.api.model.FollowerResponse
import com.tfm.musiccommunityapp.data.api.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

internal interface UsersApi {

    companion object {
        const val API_USERS_URL = BuildConfig.BACKEND_URL + "users"
    }

    //region User profiles
    @GET(API_USERS_URL)
    suspend fun getAllUsers(): Response<List<UserResponse>>

    @GET("${API_USERS_URL}/user")
    suspend fun getUser(@Query("login") login: String?): Response<UserResponse?>

    @PUT("${API_USERS_URL}/user")
    suspend fun updateUserInfo(@Body userToUpdate: UserResponse): Response<UserResponse>

    //endregion

    //region Followers

    @GET("${API_USERS_URL}/user/followers")
    suspend fun getUserFollowers(@Query("login") login: String?): Response<List<FollowerResponse>>

    @GET("${API_USERS_URL}/user/following")
    suspend fun getUserFollowing(): Response<List<FollowerResponse>>

    @GET("${API_USERS_URL}/user/amIFollower")
    suspend fun isUserFollowerOf(@Query("user") user: String): Response<Boolean>

    @POST("${API_USERS_URL}/user/followers")
    suspend fun followUser(@Query("subscribeTo") subscribeTo: String): Response<Unit>

    @POST("${API_USERS_URL}/user/followers/unfollow")
    suspend fun unfollowUser(@Query("unsubscribeFrom") unsubscribeFrom: String): Response<Unit>

    //endregion

}