package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.api.PostsApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.api.model.toRequest
import com.tfm.musiccommunityapp.data.datasource.DiscussionDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

internal class DiscussionRemoteDatasourceImpl(
    private val discussionApi: PostsApi
): DiscussionDatasource {
    override suspend fun getAllDiscussions(keyword: String?): Either<DomainError, List<DiscussionDomain>> = eitherOf(
        request = { discussionApi.getDiscussions(keyword) },
        mapper = { it?.map { discussion -> discussion.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getDiscussionById(discussionId: Long): Either<DomainError, DiscussionDomain?> = eitherOf(
        request = { discussionApi.getDiscussionById(discussionId) },
        mapper = { it?.toDomain() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun createDiscussion(discussion: DiscussionDomain): Either<DomainError, Long> = eitherOf(
        request = { discussionApi.createDiscussion(discussion.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun updateDiscussion(discussion: DiscussionDomain): Either<DomainError, Long> = eitherOf(
        request = { discussionApi.updateDiscussion(discussion.id, discussion.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun deleteDiscussion(discussionId: Long): Either<DomainError, Unit> = eitherOf(
        request = { discussionApi.deleteDiscussionById(discussionId) },
        mapper = { }
    ) { error -> error.toErrorResponse().toDomain() }
}