package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.DiscussionDatasource
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.usecase.repository.DiscussionRepository

class DiscussionRepositoryImpl(
    private val discussionDatasource: DiscussionDatasource
): DiscussionRepository {
    override suspend fun getDiscussions(keyword: String?): Either<DomainError, List<DiscussionDomain>> =
        discussionDatasource.getAllDiscussions(keyword)

    override suspend fun getDiscussionById(discussionId: Long): Either<DomainError, DiscussionDomain?> =
        discussionDatasource.getDiscussionById(discussionId)

    override suspend fun createDiscussion(discussion: DiscussionDomain): Either<DomainError, Long> =
        discussionDatasource.createDiscussion(discussion)

    override suspend fun updateDiscussion(discussion: DiscussionDomain): Either<DomainError, Long> =
        discussionDatasource.updateDiscussion(discussion)

    override suspend fun deleteDiscussion(discussionId: Long): Either<DomainError, Unit> =
        discussionDatasource.deleteDiscussion(discussionId)

}