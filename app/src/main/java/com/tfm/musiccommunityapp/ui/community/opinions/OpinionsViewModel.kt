package com.tfm.musiccommunityapp.ui.community.opinions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.model.OpinionDomain
import com.tfm.musiccommunityapp.ui.utils.SingleLiveEvent
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionsResult
import com.tfm.musiccommunityapp.usecase.opinion.GetOpinionsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class OpinionsViewModel(
    private val getOpinions: GetOpinionsUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _opinions: MutableLiveData<List<OpinionDomain>> = MutableLiveData()
    private val _communityOpinionsError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showOpinionsLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getOpinionsLiveData() = _opinions as LiveData<List<OpinionDomain>>
    fun getCommunityOpinionsError() = _communityOpinionsError as LiveData<String>
    fun isOpinionsLoading() = _showOpinionsLoader as LiveData<Boolean>

    fun setUpOpinions(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            _showOpinionsLoader.postValue(true)
            handleGetOpinionsResult(getOpinions(searchTerm))
        }
    }

    private fun handleGetOpinionsResult(result: GetOpinionsResult) {
        when (result) {
            is GetOpinionsResult.Success -> {
                _opinions.postValue(result.opinions)
            }
            is GetOpinionsResult.NetworkError -> {
                _opinions.postValue(emptyList())
                sendCommunityOpinionsError(result.error.code, result.error.message)
            }
            is GetOpinionsResult.GenericError -> {
                _opinions.postValue(emptyList())
                sendCommunityOpinionsError(result.error.code, result.error.message)
            }
        }
        _showOpinionsLoader.postValue(false)
    }

    private fun sendCommunityOpinionsError(code: Int, message: String) {
        _communityOpinionsError.postValue("Error code: $code - $message")
    }
}