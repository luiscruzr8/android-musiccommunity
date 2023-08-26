package com.tfm.musiccommunityapp.ui.community.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventsResult
import com.tfm.musiccommunityapp.domain.interactor.event.GetEventsUseCase
import com.tfm.musiccommunityapp.domain.model.EventDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class EventsViewModel(
    private val getEvents: GetEventsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _events: MutableLiveData<List<EventDomain>> = MutableLiveData()
    private val _communityEventsError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showEventsLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getEventsLiveData() = _events as LiveData<List<EventDomain>>
    fun getCommunityEventsError() = _communityEventsError as LiveData<String>
    fun isEventsLoading() = _showEventsLoader as LiveData<Boolean>

    fun setUpEvents(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            _showEventsLoader.postValue(true)
            handleGetEventsResult(getEvents(searchTerm))
        }
    }

    private fun handleGetEventsResult(result: GetEventsResult) {
        when (result) {
            is GetEventsResult.Success -> {
                _events.postValue(result.events)
            }
            is GetEventsResult.NetworkError -> {
                _events.postValue(emptyList())
                sendCommunityEventsError(result.error.code, result.error.message)
            }
            is GetEventsResult.GenericError -> {
                _events.postValue(emptyList())
                sendCommunityEventsError(result.error.code, result.error.message)
            }
        }
        _showEventsLoader.postValue(false)
    }

    private fun sendCommunityEventsError(code: Int, message: String) {
        _communityEventsError.postValue("Error code: $code - $message")
    }
}