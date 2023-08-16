package com.tfm.musiccommunityapp.domain.repository

import arrow.core.Either
import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.EventDomain

interface EventRepository {

    suspend fun getEvents(keyword: String?) : Either<DomainError, List<EventDomain>>

    suspend fun getEventById(eventId: Long): Either<DomainError, EventDomain?>

    suspend fun createEvent(event: EventDomain): Either<DomainError, Long>

    suspend fun updateEvent(event: EventDomain): Either<DomainError, Long>

    suspend fun deleteEvent(eventId: Long): Either<DomainError, Unit>

}