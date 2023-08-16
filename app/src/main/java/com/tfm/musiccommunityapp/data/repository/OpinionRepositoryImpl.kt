package com.tfm.musiccommunityapp.data.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.data.datasource.OpinionDatasource
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.domain.repository.OpinionRepository

class OpinionRepositoryImpl(
    private val opinionDatasource: OpinionDatasource
): OpinionRepository {
    override suspend fun getOpinions(keyword: String?): Either<DomainError, List<OpinionDomain>> =
        opinionDatasource.getAllOpinions(keyword)

    override suspend fun getOpinionById(opinionId: Long): Either<DomainError, OpinionDomain?> =
        opinionDatasource.getOpinionById(opinionId)

    override suspend fun createOpinion(opinion: OpinionDomain): Either<DomainError, Long> =
        opinionDatasource.createOpinion(opinion)

    override suspend fun updateOpinion(opinion: OpinionDomain): Either<DomainError, Long> =
        opinionDatasource.updateOpinion(opinion)

    override suspend fun deleteOpinion(opinionId: Long): Either<DomainError, Unit> =
        opinionDatasource.deleteOpinion(opinionId)
}