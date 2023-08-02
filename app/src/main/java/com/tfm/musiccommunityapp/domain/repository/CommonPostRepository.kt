package com.tfm.musiccommunityapp.domain.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain

interface CommonPostRepository {

    //region Common

    suspend fun getUserPosts(
        login: String,
        type: String?,
        keyword: String?
    ) : Either<DomainError, List<GenericPostDomain>>

    suspend fun getPostsByCoordinates(
        latitude: Double,
        longitude: Double,
        checkClosest: Boolean
    ) : Either<DomainError, List<GenericPostDomain>>

    suspend fun getPostsByCity(city: String): Either<DomainError, List<GenericPostDomain>>

    suspend fun getPostImage(postId: Long): Either<DomainError, String>



    //endregion

    //region Comments

    suspend fun getPostComments(postId: Long): Either<DomainError, List<CommentDomain>>

    suspend fun postComment(
        postId: Long,
        commentId: Long?,
        comment: CommentDomain
    ): Either<DomainError, Long>

    suspend fun deleteComment(postId: Long, commentId: Long): Either<DomainError, Unit>

    //endregion

}