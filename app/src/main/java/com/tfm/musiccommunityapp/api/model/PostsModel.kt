package com.tfm.musiccommunityapp.api.model

import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.domain.model.CommentDomain
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import retrofit2.http.Multipart
import java.time.LocalDate
import java.time.LocalDateTime

internal data class GenericPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val creationDateTime: LocalDateTime,
    val tags: List<TagResponse>
)

internal fun GenericPostResponse.toDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = type,
    createdOn = creationDateTime,
    tags = tags.map { it.toDomain() }
)

internal fun GenericPostDomain.toResponse() = GenericPostResponse(
    id = id,
    title = title,
    description = description,
    login = login,
    type = postType,
    creationDateTime = createdOn,
    tags = tags.map { it.toResponse() }
)

internal data class ImageRequest(
    val file: Multipart
)

internal data class CommentResponse(
    val id: Long,
    val commentText: String,
    val login: String,
    val commentDate: LocalDateTime,
    val responses: List<CommentResponse>
)

internal fun CommentResponse.toDomain(): CommentDomain = CommentDomain(
    id = id,
    text = commentText,
    login = login,
    commentedOn = commentDate,
    responses = responses.map { it.toDomain() }
)

internal fun CommentDomain.toResponse(): CommentResponse = CommentResponse(
    id = id,
    commentText = text,
    login = login,
    commentDate = commentedOn,
    responses = responses.map { it.toResponse() }
)

internal data class EventPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val cityName: String,
    val creationDateTime: LocalDateTime,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val tags: List<TagResponse>
)

internal fun EventPostResponse.toDomain() = EventDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = type,
    cityName = cityName,
    createdOn = creationDateTime,
    from = startDateTime,
    to = endDateTime,
    tags = tags.map { it.toDomain() }
)

internal fun EventDomain.toRequest() = EventPostResponse(
    id = id,
    title = title,
    description = description,
    login = login,
    type = postType,
    cityName = cityName,
    creationDateTime = createdOn,
    startDateTime = from,
    endDateTime = to,
    tags = tags.map { it.toResponse() }
)

internal data class AdvertisementPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val cityName: String,
    val contactPhone: String,
    val creationDateTime: LocalDateTime,
    val endDate: LocalDate,
    val tags: List<TagResponse>
)

internal fun AdvertisementPostResponse.toDomain() = AdvertisementDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = type,
    cityName = cityName,
    phone = contactPhone,
    createdOn = creationDateTime,
    until = endDate,
    tags = tags.map { it.toDomain() }
)

internal fun AdvertisementDomain.toRequest() = AdvertisementPostResponse(
    id = id,
    title = title,
    description = description,
    login = login,
    type = postType,
    cityName = cityName,
    contactPhone = phone,
    creationDateTime = createdOn,
    endDate = until,
    tags = tags.map { it.toResponse() }
)

internal data class DiscussionPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val creationDateTime: LocalDateTime,
    val tags: List<TagResponse>
)

internal fun DiscussionPostResponse.toDomain() = DiscussionDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = type,
    createdOn = creationDateTime,
    tags = tags.map { it.toDomain() }
)

internal fun DiscussionDomain.toRequest() = DiscussionPostResponse(
    id = id,
    title = title,
    description = description,
    login = login,
    type = postType,
    creationDateTime = createdOn,
    tags = tags.map { it.toResponse() }
)

internal data class OpinionPostResponse(
    val id: Long,
    val title: String,
    val description: String,
    val login: String,
    val type: String,
    val scoreId: Long,
    val scoreDto: ScoreResponse,
    val creationDateTime: LocalDateTime,
    val tags: List<TagResponse>
)

internal fun OpinionPostResponse.toDomain() = OpinionDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = type,
    scoreId = scoreId,
    score = scoreDto.toDomain(),
    createdOn = creationDateTime,
    tags = tags.map { it.toDomain() }
)

internal fun OpinionDomain.toRequest() = OpinionPostResponse(
    id = id,
    title = title,
    description = description,
    login = login,
    type = postType,
    scoreId = scoreId,
    scoreDto = score.toResponse(),
    creationDateTime = createdOn,
    tags = tags.map { it.toResponse() }
)

internal fun EventDomain.toGenericDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = postType,
    createdOn = createdOn,
    tags = tags
)

internal fun AdvertisementDomain.toGenericDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = postType,
    createdOn = createdOn,
    tags = tags
)

internal fun DiscussionDomain.toGenericDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = postType,
    createdOn = createdOn,
    tags = tags
)

internal fun OpinionDomain.toGenericDomain() = GenericPostDomain(
    id = id,
    title = title,
    description = description,
    login = login,
    postType = postType,
    createdOn = createdOn,
    tags = tags
)