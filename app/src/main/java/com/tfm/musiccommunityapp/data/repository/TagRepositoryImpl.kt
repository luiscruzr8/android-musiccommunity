package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.TagDatasource
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain
import com.tfm.musiccommunityapp.domain.repository.TagRepository

class TagRepositoryImpl(
    private val tagDatasource: TagDatasource
): TagRepository {

    override suspend fun getAllTags(): Either<DomainError, List<TagDomain>> =
        tagDatasource.getAllTags()

    override suspend fun getUsersByTag(tagName: String): Either<DomainError, List<ShortUserDomain>> =
        tagDatasource.getUsersByTag(tagName)

    override suspend fun getPostsByTag(tagName: String): Either<DomainError, List<GenericPostDomain>> =
        tagDatasource.getPostsByTag(tagName)

}