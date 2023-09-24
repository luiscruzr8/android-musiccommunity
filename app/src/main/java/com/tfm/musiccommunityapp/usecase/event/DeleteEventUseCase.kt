package com.tfm.musiccommunityapp.usecase.event

import com.tfm.musiccommunityapp.domain.model.DomainError
import com.tfm.musiccommunityapp.domain.model.NetworkError
import com.tfm.musiccommunityapp.usecase.repository.EventRepository

interface DeleteEventUseCase {
    suspend operator fun invoke(eventId: Long) : DeleteEventResult
}

class DeleteEventUseCaseImpl(
    private val eventRepository: EventRepository
) : DeleteEventUseCase {
    override suspend operator fun invoke(eventId: Long) : DeleteEventResult =
        eventRepository.deleteEvent(eventId).fold(
            { it.toErrorResult() },
            { DeleteEventResult.Success }
        )
}

private fun DomainError.toErrorResult() = when (this) {
    is NetworkError -> DeleteEventResult.NetworkError(this)
    else -> DeleteEventResult.GenericError(this)
}

sealed interface DeleteEventResult {
    object Success : DeleteEventResult
    data class NetworkError(val error: DomainError) : DeleteEventResult
    data class GenericError(val error: DomainError) : DeleteEventResult
}