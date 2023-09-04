package com.tfm.musiccommunityapp.usecase.event

import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.usecase.repository.EventRepository

interface GetEventByIdUseCase {
    suspend operator fun invoke(eventId: Long) : GetEventByIdResult
}

class GetEventByIdUseCaseImpl(
    private val eventRepository: EventRepository
) : GetEventByIdUseCase {
    override suspend fun invoke(eventId: Long) : GetEventByIdResult =
        eventRepository.getEventById(eventId).fold(
            { toErrorResult() },
            { GetEventByIdResult.Success(it) }
        )
}

private fun toErrorResult() = GetEventByIdResult.NoEvent

sealed interface GetEventByIdResult {
    data class Success(val event: EventDomain?) : GetEventByIdResult
    object NoEvent : GetEventByIdResult
}