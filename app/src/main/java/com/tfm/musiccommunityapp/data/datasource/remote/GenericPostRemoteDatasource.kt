package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.PostsApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.api.model.toResponse
import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain

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
