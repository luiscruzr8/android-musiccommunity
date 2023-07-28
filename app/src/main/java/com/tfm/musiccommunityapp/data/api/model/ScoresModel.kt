package com.tfm.musiccommunityapp.data.api.model

import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import java.time.LocalDateTime

internal data class ScoreResponse(
    val id: Long,
    val scoreName: String,
    val fileType: String,
    val login: String,
    val uploadDateTime: LocalDateTime
)

internal fun ScoreResponse.toDomain() = ScoreDomain(
    id = id,
    name = scoreName,
    login = login,
    uploadedOn = uploadDateTime
)