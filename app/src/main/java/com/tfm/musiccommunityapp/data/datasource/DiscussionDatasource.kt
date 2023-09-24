package com.tfm.musiccommunityapp.data.datasource

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError

interface DiscussionDatasource {

    suspend fun getAllDiscussions(keyword: String?): Either<DomainError, List<DiscussionDomain>>

    suspend fun getDiscussionById(discussionId: Long): Either<DomainError, DiscussionDomain?>

    suspend fun createDiscussion(discussion: DiscussionDomain): Either<DomainError, Long>

    suspend fun updateDiscussion(discussion: DiscussionDomain): Either<DomainError, Long>

    suspend fun deleteDiscussion(discussionId: Long): Either<DomainError, Unit>
}