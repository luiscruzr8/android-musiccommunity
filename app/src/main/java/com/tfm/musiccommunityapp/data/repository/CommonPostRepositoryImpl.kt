package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.GenericPostDatasource
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.usecase.repository.CommonPostRepository
import java.io.File

class CommonPostRepositoryImpl(
    private val commonDatasource: GenericPostDatasource
): CommonPostRepository {
    override suspend fun getUserPosts(login: String, type: String?, keyword: String?):
            Either<DomainError, List<GenericPostDomain>> =
        commonDatasource.getUserPosts(login, type, keyword)

    override suspend fun getPostsByCoordinates(
        latitude: Double,
        longitude: Double,
        checkClosest: Boolean
    ): Either<DomainError, List<GenericPostDomain>> =
        commonDatasource.getPostsByCoordinates(latitude, longitude, checkClosest)

    override suspend fun getPostsByCity(city: String): Either<DomainError, List<GenericPostDomain>> =
        commonDatasource.getPostsByCity(city)

    override suspend fun getPostImage(postId: Long): Either<DomainError, String> =
        commonDatasource.getPostImage(postId)

    override suspend fun uploadPostImage(postId: Long, image: File): Either<DomainError, Long> =
        commonDatasource.uploadPostImage(postId, image)

    override suspend fun getPostComments(postId: Long): Either<DomainError, List<CommentDomain>> =
        commonDatasource.getPostComments(postId)

    override suspend fun postComment(postId: Long, commentId: Long?, comment: CommentDomain):
            Either<DomainError, Long> =
        commonDatasource.postComment(postId, commentId, comment)

    override suspend fun deleteComment(postId: Long, commentId: Long): Either<DomainError, Unit> =
        commonDatasource.deleteComment(postId, commentId)
}