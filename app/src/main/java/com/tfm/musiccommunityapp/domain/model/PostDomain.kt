package com.tfm.musiccommunityapp.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class GenericPostDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val postType: String,
    val createdOn: LocalDateTime,
    val tags: List<TagDomain>
)

data class CommentDomain(
    val id: Long,
    val text: String,
    val login: String,
    val commentedOn: LocalDateTime,
    val responses: List<CommentDomain>
)

data class EventDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val postType: String,
    val cityName: String,
    val createdOn: LocalDateTime,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val tags: List<TagDomain>
)

data class AdvertisementDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val postType: String,
    val cityName: String,
    val phone: String,
    val createdOn: LocalDateTime,
    val until: LocalDate,
    val tags: List<TagDomain>
)

data class DiscussionDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val postType: String,
    val createdOn: LocalDateTime,
    val tags: List<TagDomain>
)

data class OpinionDomain(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val postType: String,
    val scoreId: Long,
    val score: ScoreDomain,
    val createdOn: LocalDateTime,
    val tags: List<TagDomain>
)