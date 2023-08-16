package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.TagsApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.datasource.TagDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain

internal class TagRemoteDatasourceImpl(
    private val tagsApi: TagsApi
) : TagDatasource {
    override suspend fun getAllTags(): Either<DomainError, List<TagDomain>> = eitherOf(
        response = tagsApi.getAllTags(),
        mapper = { result -> result?.map { it.toDomain() } ?: emptyList() },
        errorMapper = { error -> error.toErrorResponse().toDomain() }
    )

    override suspend fun getUsersByTag(tagName: String): Either<DomainError, List<ShortUserDomain>> =
        eitherOf(
            response = tagsApi.getUsersByTag(tagName),
            mapper = { result -> result?.map { it.toDomain() } ?: emptyList() },
            errorMapper = { error -> error.toErrorResponse().toDomain() }
        )

    override suspend fun getPostsByTag(tagName: String): Either<DomainError, List<GenericPostDomain>> =
        eitherOf(
            response = tagsApi.getPostsByTag(tagName),
            mapper = { result -> result?.map { it.toDomain() } ?: emptyList() },
            errorMapper = { error -> error.toErrorResponse().toDomain() }
        )

}