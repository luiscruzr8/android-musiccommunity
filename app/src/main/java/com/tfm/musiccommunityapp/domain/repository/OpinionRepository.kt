package com.tfm.musiccommunityapp.domain.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.OpinionDomain

interface OpinionRepository {

    suspend fun getOpinions(keyword: String?): Either<DomainError, List<OpinionDomain>>

    suspend fun getOpinionById(opinionId: Long): Either<DomainError, OpinionDomain?>

    suspend fun createOpinion(opinion: OpinionDomain): Either<DomainError, Long>

    suspend fun updateOpinion(opinion: OpinionDomain): Either<DomainError, Long>

    suspend fun deleteOpinion(opinionId: Long): Either<DomainError, Unit>

}