package com.tfm.musiccommunityapp.api

import com.tfm.musiccommunityapp.BuildConfig
import com.tfm.musiccommunityapp.api.model.AdvertisementPostResponse
import com.tfm.musiccommunityapp.api.model.CommentResponse
import com.tfm.musiccommunityapp.api.model.DiscussionPostResponse
import com.tfm.musiccommunityapp.api.model.EventPostResponse
import com.tfm.musiccommunityapp.api.model.GenericPostResponse
import com.tfm.musiccommunityapp.api.model.OpinionPostResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PostsApi {

    companion object {
        const val API_POSTS_URL = BuildConfig.BACKEND_URL + "posts"
        const val POST_IMAGE = BuildConfig.BACKEND_URL + "posts/%s/img"
    }

    //region Common posts

    @GET("$API_POSTS_URL/user")
    suspend fun getUserPosts(
        @Query("login") login: String,
        @Query("type") type: String? = null,
        @Query("keyword") keyword: String? = null,
    ): Response<List<GenericPostResponse>>

    @GET("$API_POSTS_URL/geo")
    suspend fun getPostsByCoordinates(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("closest") closest: Boolean,
    ): Response<List<GenericPostResponse>>

    @GET("$API_POSTS_URL/city")
    suspend fun getPostsByCityName(
        @Query("cityName") cityName: String,
    ): Response<List<GenericPostResponse>>

    @Multipart
    @POST("$API_POSTS_URL/{id}/img")
    suspend fun uploadPostImage(
        @Path("id") id: String,
        @Part("img") image: MultipartBody.Part,
    ): Response<Long>

    //endregion

    //region Comments

    @GET("$API_POSTS_URL/{postId}/comments")
    suspend fun getPostComments(
        @Path("postId") postId: Long,
    ): Response<List<CommentResponse>>

    @POST("$API_POSTS_URL/{postId}/comments")
    suspend fun postComment(
        @Path("postId") postId: Long,
        @Query("responseTo") responseTo: Long? = null,
        @Body comment: CommentResponse,
    ): Response<Long>

    @DELETE("$API_POSTS_URL/{postId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("postId") postId: Long,
        @Path("commentId") commentId: Long,
    ): Response<Unit>

    //endregion

    //region Events

    @GET("$API_POSTS_URL/events")
    suspend fun getEvents(
        @Query("keyword") keyword: String?,
    ): Response<List<EventPostResponse>>

    @GET("$API_POSTS_URL/events/{id}")
    suspend fun getEventById(
        @Path("id") id: Long,
    ): Response<EventPostResponse>

    @POST("$API_POSTS_URL/events")
    suspend fun createEvent(
        @Body event: EventPostResponse,
    ): Response<Long>

    @PUT("$API_POSTS_URL/events/{id}")
    suspend fun updateEvent(
        @Path("id") id: Long,
        @Body updatedEvent: EventPostResponse,
    ): Response<Long>

    @DELETE("$API_POSTS_URL/events/{id}")
    suspend fun deleteEventById(
        @Path("id") id: Long,
    ): Response<Unit>

    //endregion

    //region Advertisements

    @GET("$API_POSTS_URL/announcements")
    suspend fun getAdvertisements(
        @Query("keyword") keyword: String?,
    ): Response<List<AdvertisementPostResponse>>

    @GET("$API_POSTS_URL/announcements/{id}")
    suspend fun getAdvertisementById(
        @Path("id") id: Long,
    ): Response<AdvertisementPostResponse>

    @POST("$API_POSTS_URL/announcements")
    suspend fun createAdvertisement(
        @Body advertisement: AdvertisementPostResponse,
    ): Response<Long>

    @PUT("$API_POSTS_URL/announcements/{id}")
    suspend fun updateAdvertisement(
        @Path("id") id: Long,
        @Body updatedAdvertisement: AdvertisementPostResponse,
    ): Response<Long>

    @DELETE("$API_POSTS_URL/announcements/{id}")
    suspend fun deleteAdvertisementById(
        @Path("id") id: Long,
    ): Response<Unit>

    //endregion

    //region Discussions

    @GET("$API_POSTS_URL/discussions")
    suspend fun getDiscussions(
        @Query("keyword") keyword: String?,
    ): Response<List<DiscussionPostResponse>>

    @GET("$API_POSTS_URL/discussions/{id}")
    suspend fun getDiscussionById(
        @Path("id") id: Long,
    ): Response<DiscussionPostResponse>

    @POST("$API_POSTS_URL/discussions")
    suspend fun createDiscussion(
        @Body discussion: DiscussionPostResponse,
    ): Response<Long>

    @PUT("$API_POSTS_URL/discussions/{id}")
    suspend fun updateDiscussion(
        @Path("id") id: Long,
        @Body updatedDiscussion: DiscussionPostResponse,
    ): Response<Long>

    @DELETE("$API_POSTS_URL/discussions/{id}")
    suspend fun deleteDiscussionById(
        @Path("id") id: Long,
    ): Response<Unit>

    //endregion

    //region Opinions

    @GET("$API_POSTS_URL/opinions")
    suspend fun getOpinions(
        @Query("keyword") keyword: String?,
    ): Response<List<OpinionPostResponse>>

    @GET("$API_POSTS_URL/opinions/{id}")
    suspend fun getOpinionById(
        @Path("id") id: Long,
    ): Response<OpinionPostResponse>

    @POST("$API_POSTS_URL/opinions")
    suspend fun createOpinion(
        @Body updatedOpinion: OpinionPostResponse,
    ): Response<Long>

    @PUT("$API_POSTS_URL/opinions/{id}")
    suspend fun updateOpinion(
        @Path("id") id: Long,
        @Body updatedOpinion: OpinionPostResponse,
    ): Response<Long>

    @DELETE("$API_POSTS_URL/opinions/{id}")
    suspend fun deleteOpinionById(
        @Path("id") id: Long,
    ): Response<Unit>

    //endregion

}