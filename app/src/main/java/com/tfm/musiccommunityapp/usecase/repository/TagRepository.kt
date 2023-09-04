package com.tfm.musiccommunityapp.usecase.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.ShortUserDomain
import com.tfm.musiccommunityapp.domain.model.TagDomain

interface TagRepository {

    //region Tags

    suspend fun getAllTags(): Either<DomainError, List<TagDomain>>

    //endregion

    //region SearchByTag

    suspend fun getUsersByTag(tagName: String): Either<DomainError, List<ShortUserDomain>>

    suspend fun getPostsByTag(tagName: String): Either<DomainError, List<GenericPostDomain>>

    //endregion
}