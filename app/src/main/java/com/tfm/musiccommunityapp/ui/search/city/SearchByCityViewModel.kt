package com.tfm.musiccommunityapp.ui.search.city

import androidx.lifecycle.ViewModel
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCityUseCase
import com.tfm.musiccommunityapp.domain.interactor.post.GetPostsByCoordinatesUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SearchByCityViewModel(
    private val searchPostsByCityName: GetPostsByCityUseCase,
    private val searchPostsByCoordinates: GetPostsByCoordinatesUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

}