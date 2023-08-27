package com.tfm.musiccommunityapp.ui.community.events.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdResult
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val getEventById: GetEventByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _event: MutableLiveData<EventDomain?> = MutableLiveData()
    private val _eventImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getEventByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showEventLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getEventLiveData() = _event as LiveData<EventDomain?>
    fun getPostImageLiveData() = _eventImageURL as LiveData<String>
    fun getEventByIdError() = _getEventByIdError as LiveData<String>
    fun isEventLoading() = _showEventLoader as LiveData<Boolean>

    fun setUpEvent(eventId: Long) {
        viewModelScope.launch(dispatcher) {
            _showEventLoader.postValue(true)
            handleGetEventByIdResult(getEventById(eventId))
            handleGetPostImageResult(getPostImageByPostId(eventId))
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _eventImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetEventByIdResult(result: GetEventByIdResult) {
        when (result) {
            GetEventByIdResult.NoEvent -> {
                _getEventByIdError.postValue("Error code: 404 - Event not found")
            }

            is GetEventByIdResult.Success -> {
                _event.postValue(result.event)
            }
        }
        _showEventLoader.postValue(false)
    }

}