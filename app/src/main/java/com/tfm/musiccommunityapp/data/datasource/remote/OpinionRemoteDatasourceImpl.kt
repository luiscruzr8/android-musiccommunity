package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.api.PostsApi
import com.tfm.musiccommunityapp.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.api.model.toDomain
import com.tfm.musiccommunityapp.api.model.toRequest
import com.tfm.musiccommunityapp.data.datasource.OpinionDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain

internal class OpinionRemoteDatasourceImpl(
    private val opinionApi: PostsApi
) : OpinionDatasource {
    override suspend fun getAllOpinions(keyword: String?): Either<DomainError, List<OpinionDomain>> = eitherOf(
        request = { opinionApi.getOpinions(keyword) },
        mapper = { it?.map { opinion -> opinion.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getOpinionById(opinionId: Long): Either<DomainError, OpinionDomain?> = eitherOf(
        request = { opinionApi.getOpinionById(opinionId) },
        mapper = { it?.toDomain() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun createOpinion(opinion: OpinionDomain): Either<DomainError, Long> = eitherOf(
        request = { opinionApi.createOpinion(opinion.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun updateOpinion(opinion: OpinionDomain): Either<DomainError, Long> = eitherOf(
        request = { opinionApi.updateOpinion(opinion.id, opinion.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun deleteOpinion(opinionId: Long): Either<DomainError, Unit> = eitherOf(
        request = { opinionApi.deleteOpinionById(opinionId) },
        mapper = { }
    ) { error -> error.toErrorResponse().toDomain() }
}