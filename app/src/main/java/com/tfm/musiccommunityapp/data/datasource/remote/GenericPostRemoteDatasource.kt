package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import arrow.core.right
import com.tfm.musiccommunityapp.data.api.PostsApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.api.model.toResponse
import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class GenericPostRemoteDatasource(
    private val postsApi: PostsApi
): GenericPostDatasource {

    //region Common

    override suspend fun getUserPosts(
        login: String,
        type: String?,
        keyword: String?
    ): Either<DomainError, List<GenericPostDomain>> =
        eitherOf(
            response = postsApi.getUserPosts(login, type, keyword),
            mapper = { posts -> posts?.map { it.toDomain() } ?: emptyList() }
        ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getPostsByCoordinates(
        latitude: Double,
        longitude: Double,
        checkClosest: Boolean
    ): Either<DomainError, List<GenericPostDomain>> =
        eitherOf(
            response = postsApi.getPostsByCoordinates(latitude, longitude, checkClosest),
            mapper = { posts -> posts?.map { it.toDomain() } ?: emptyList() }
        ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getPostsByCity(city: String): Either<DomainError, List<GenericPostDomain>> =
        eitherOf(
            response = postsApi.getPostsByCityName(city),
            mapper = { posts -> posts?.map { it.toDomain() } ?: emptyList() }
        ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getPostImage(postId: Long): Either<DomainError, String> =
        String.format(PostsApi.POST_IMAGE, postId).right()

    override suspend fun uploadPostImage(postId: Long, image: File): Either<DomainError, Long> {
        //TODO: TRANSFORM FILE TO MULTIPART THEN RETURN RESPONSE
        return try {
            val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("img", image.name, requestFile)

            eitherOf(
                response = postsApi.uploadPostImage(postId.toString(), imagePart),
                mapper = { it ?: -1L }
            ) { error -> error.toErrorResponse().toDomain() }
        } catch (e: Exception) {
            Either.Left(
                GenericError(
                    "Converting image error",
                    e.message ?: "Error trying to convert image while sending it",
                    888,
                    e
                )
            )
        }
    }

    //endregion

    //region Comments

    override suspend fun getPostComments(postId: Long): Either<DomainError, List<CommentDomain>> =
        eitherOf(
            response = postsApi.getPostComments(postId),
            mapper = { posts -> posts?.map { it.toDomain() } ?: emptyList() }
        ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun postComment(
        postId: Long,
        commentId: Long?,
        comment: CommentDomain
    ): Either<DomainError, Long> =
        eitherOf(
            response = postsApi.postComment(postId, commentId, comment.toResponse()),
            mapper = { it ?: -1L }
        ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun deleteComment(postId: Long, commentId: Long): Either<DomainError, Unit> =
        eitherOf(
            response = postsApi.deleteComment(postId, commentId),
            mapper = { }
        ) { error -> error.toErrorResponse().toDomain() }

    //endregion

}
