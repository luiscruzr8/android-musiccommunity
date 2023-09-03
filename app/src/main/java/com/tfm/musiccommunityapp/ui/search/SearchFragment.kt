package com.tfm.musiccommunityapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.SearchFragmentBinding
import com.tfm.musiccommunityapp.ui.search.city.SearchByCityFragmentDirections
import com.tfm.musiccommunityapp.utils.viewBinding

class SearchFragment: BaseFragment(R.layout.search_fragment) {

    private val binding by viewBinding(SearchFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //Main buttons
            restartButton.setOnClickListener {
                changeMainButtonsVisibility(true)
                changeTagButtonsVisibility(false)
                changeCityButtonsVisibility(false)
            }

            // Search by Tag
            searchByTagButton.setOnClickListener {
                changeMainButtonsVisibility(false)
                changeTagButtonsVisibility(true)
            }
            searchPostsByTagButton.setOnClickListener {
                //search
            }
            searchByUserInterestButton.setOnClickListener {
                //search
            }

            // Search by City
            searchByCityButton.setOnClickListener {
                changeMainButtonsVisibility(false)
                changeCityButtonsVisibility(true)
            }
            searchPostsByCoordinatesButton.setOnClickListener {

            }
            searchPostsByCityNameButton.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToSearchByCityFragment()
                navigateSafe(action)
            }

        }
    }

    private fun changeMainButtonsVisibility(isVisible: Boolean) {
        binding.apply {
            restartButton.isVisible = !isVisible
            tvChooseDifferentSearchType.isVisible = !isVisible
            searchByTagButton.isVisible = isVisible
            searchByCityButton.isVisible = isVisible
        }
    }

    private fun changeTagButtonsVisibility(isVisible: Boolean) {
        binding.apply {
            searchByUserInterestButton.isVisible = isVisible
            searchPostsByTagButton.isVisible = isVisible
        }
    }

    private fun changeCityButtonsVisibility(isVisible: Boolean) {
        binding.apply {
            searchPostsByCityNameButton.isVisible = isVisible
            searchPostsByCoordinatesButton.isVisible = isVisible
        }
    }
}