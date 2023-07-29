package com.tfm.musiccommunityapp.data.datasource.remote

import arrow.core.Either
import com.tfm.musiccommunityapp.data.api.PostsApi
import com.tfm.musiccommunityapp.data.api.extensions.toErrorResponse
import com.tfm.musiccommunityapp.data.api.model.toDomain
import com.tfm.musiccommunityapp.data.api.model.toRequest
import com.tfm.musiccommunityapp.data.datasource.EventDatasource
import com.tfm.musiccommunityapp.data.extensions.eitherOf
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.EventDomain

internal class EventRemoteDatasourceImpl(
    private val eventApi: PostsApi
): EventDatasource {
    override suspend fun getAllEvents(keyword: String?): Either<DomainError, List<EventDomain>> = eitherOf(
        request = { eventApi.getEvents(keyword) },
        mapper = { it?.map { event -> event.toDomain() } ?: emptyList() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun getEventById(eventId: Long): Either<DomainError, EventDomain?> = eitherOf(
        request = { eventApi.getEventById(eventId) },
        mapper = { it?.toDomain() }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun createEvent(event: EventDomain): Either<DomainError, Long> = eitherOf(
        request = { eventApi.createEvent(event.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun updateEvent(event: EventDomain): Either<DomainError, Long> = eitherOf(
        request = { eventApi.updateEvent(event.id, event.toRequest()) },
        mapper = { it ?: -1L }
    ) { error -> error.toErrorResponse().toDomain() }

    override suspend fun deleteEvent(eventId: Long): Either<DomainError, Unit> = eitherOf(
        request = { eventApi.deleteEventById(eventId) },
        mapper = { }
    ) { error -> error.toErrorResponse().toDomain() }
}