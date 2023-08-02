package com.tfm.musiccommunityapp.data.repository

import com.tfm.musiccommunityapp.data.datasource.EventDatasource
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventDatasource: EventDatasource
): EventRepository {

    override suspend fun getEvents(keyword: String?) = eventDatasource.getAllEvents(keyword)

    override suspend fun getEventById(eventId: Long) = eventDatasource.getEventById(eventId)

    override suspend fun createEvent(event: EventDomain) = eventDatasource.createEvent(event)

    override suspend fun updateEvent(event: EventDomain) =  eventDatasource.updateEvent(event)

    override suspend fun deleteEvent(eventId: Long) = eventDatasource.deleteEvent(eventId)

}