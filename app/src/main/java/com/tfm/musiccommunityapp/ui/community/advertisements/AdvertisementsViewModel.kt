package com.tfm.musiccommunityapp.ui.community.advertisements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementsResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementsUseCase
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class AdvertisementsViewModel(
    private val getAdvertisements: GetAdvertisementsUseCase,
    private  val dispatcher: CoroutineDispatcher
) : ViewModel(){
    private val _advertisements: MutableLiveData<List<AdvertisementDomain>> = MutableLiveData()
    private val _communityAdvertisementsError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showAdvertisementsLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getAdvertisementsLiveData() = _advertisements as LiveData<List<AdvertisementDomain>>
    fun getCommunityAdvertisementsError() = _communityAdvertisementsError as LiveData<String>
    fun isAdvertisementsLoading() = _showAdvertisementsLoader as LiveData<Boolean>

    fun setUpAdvertisements(searchTerm: String?) {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementsLoader.postValue(true)
            handleGetAdvertisementsResult(getAdvertisements(searchTerm))
        }
    }

    private fun handleGetAdvertisementsResult(result: GetAdvertisementsResult) {
        when (result) {
            is GetAdvertisementsResult.Success -> {
                _advertisements.postValue(result.advertisements)
            }
            is GetAdvertisementsResult.NetworkError -> {
                _advertisements.postValue(emptyList())
                sendCommunityAdvertisementsError(result.error.code, result.error.message)
            }
            is GetAdvertisementsResult.GenericError -> {
                _advertisements.postValue(emptyList())
                sendCommunityAdvertisementsError(result.error.code, result.error.message)
            }
        }
        _showAdvertisementsLoader.postValue(false)
    }

    private fun sendCommunityAdvertisementsError(code: Int, message: String) {
        _communityAdvertisementsError.postValue("Error code: $code - $message")
    }

}