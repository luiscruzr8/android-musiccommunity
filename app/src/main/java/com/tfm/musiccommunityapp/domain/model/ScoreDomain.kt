package com.tfm.musiccommunityapp.domain.model

import java.time.LocalDateTime

data class ScoreDomain(
    val id: Long,
    val name: String,
    val login: String,
    val uploadedOn: LocalDateTime
)