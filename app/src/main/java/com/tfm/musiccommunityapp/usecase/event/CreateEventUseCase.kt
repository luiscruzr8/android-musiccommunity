package com.tfm.musiccommunityapp.usecase.event

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.EventRepository

interface CreateEventUseCase {
    suspend operator fun invoke(newEvent: EventDomain) : CreateEventResult
}

class CreateEventUseCaseImpl(
    private val eventRepository: EventRepository
) : CreateEventUseCase {
    override suspend operator fun invoke(newEvent: EventDomain) : CreateEventResult =
        eventRepository.createEvent(newEvent).fold(
            { it.toErrorResult() },
            { CreateEventResult.Success(it) }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> CreateEventResult.NetworkError(this)
    else -> CreateEventResult.GenericError(this)
}

sealed interface CreateEventResult {
    data class Success(val id: Long) : CreateEventResult
    data class NetworkError(val error: DomainError) : CreateEventResult
    data class GenericError(val error: DomainError) : CreateEventResult
}