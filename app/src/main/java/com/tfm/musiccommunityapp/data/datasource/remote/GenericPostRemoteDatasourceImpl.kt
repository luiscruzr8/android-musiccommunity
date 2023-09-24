package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import arrow.core.right
import com.tfm.musiccommunityapp.api.PostsApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.api.model.toResponse
import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class GenericPostRemoteDatasourceImpl(
    private val postsApi: PostsApi
) : GenericPostDatasource {

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

    override suspend fun uploadPostImage(postId: Long, image: File): Either<DomainError, Long> =
        eitherOf(
            request = {
                val requestFile = image.asRequestBody("image/jpg".toMediaTypeOrNull())
                val imageBody = MultipartBody.Part.createFormData("img", image.name, requestFile)
                postsApi.uploadPostImage(postId, imageBody)
            },
            mapper = { it ?: -1L },
            errorMapper = { it.toErrorResponse().toDomain() }
        )

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
