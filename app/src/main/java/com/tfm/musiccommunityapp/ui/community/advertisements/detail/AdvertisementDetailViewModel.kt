package com.tfm.musiccommunityapp.ui.community.advertisements.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdResult
import com.tfm.musiccommunityapp.domain.interactor.advertisement.GetAdvertisementByIdUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageResult
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostImageUseCase
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class AdvertisementDetailViewModel(
    private val getAdvertisementById: GetAdvertisementByIdUseCase,
    private val getPostImageByPostId: GetPostImageUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _advertisement: MutableLiveData<AdvertisementDomain?> = MutableLiveData()
    private val _advertisementImageURL: SingleLiveEvent<String> = SingleLiveEvent()
    private val _getAdvertisementByIdError: SingleLiveEvent<String> = SingleLiveEvent()
    private val _showAdvertisementLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun getAdvertisementLiveData() = _advertisement as LiveData<AdvertisementDomain?>
    fun getPostImageLiveData() = _advertisementImageURL as LiveData<String>
    fun getAdvertisementByIdError() = _getAdvertisementByIdError as LiveData<String>
    fun isAdvertisementLoading() = _showAdvertisementLoader as LiveData<Boolean>

    fun setUpAdvertisement(advertisementId: Long) {
        viewModelScope.launch(dispatcher) {
            _showAdvertisementLoader.postValue(true)
            handleGetAdvertisementByIdResult(getAdvertisementById(advertisementId))
            handleGetPostImageResult(getPostImageByPostId(advertisementId))
        }
    }

    private fun handleGetPostImageResult(result: GetPostImageResult) {
        when (result) {
            is GetPostImageResult.Success -> _advertisementImageURL.postValue(result.imageUrl)
        }
    }

    private fun handleGetAdvertisementByIdResult(result: GetAdvertisementByIdResult) {
        when (result) {
            GetAdvertisementByIdResult.NoAdvertisement -> {
                _getAdvertisementByIdError.postValue("Error code: 404 - Advertisement not found")
            }

            is GetAdvertisementByIdResult.Success -> {
                _advertisement.postValue(result.advertisement)
            }
        }
        _showAdvertisementLoader.postValue(false)
    }
}