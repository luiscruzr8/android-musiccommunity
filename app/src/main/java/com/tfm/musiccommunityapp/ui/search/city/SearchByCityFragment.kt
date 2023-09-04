package com.tfm.musiccommunityapp.ui.search.city

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.SearchByCityFragmentBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.posts.UserPostsAdapter
import com.tfm.musiccommunityapp.ui.utils.navigateFromCityNameSearchOnPostType
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchByCityFragment: BaseFragment(R.layout.search_by_city_fragment) {

    private val binding by viewBinding(SearchByCityFragmentBinding::bind)
    private val viewModel by viewModel<SearchByCityViewModel>()
    private val postsAdapter = UserPostsAdapter(::onPostClicked)

    private var cityNameSearch = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(CITY_SEARCH, cityNameSearch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            cityNameSearch = savedInstanceState.getCharSequence(CITY_SEARCH, "").toString()
            binding.cityNameEditText.setText(cityNameSearch)
        }

        observeLoader()
        observeCitiesResult()
        observeCitiesError()
        observePostsResult()
        observePostsError()

        viewModel.setUpSearchByCityName(cityNameSearch)
        binding.rvPosts.apply {
            adapter = postsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        (binding.cityNameEditText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener  { parent, _, position, _ ->
                cityNameSearch= parent.getItemAtPosition(position) as String
            }

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpSearchByCityName(cityNameSearch)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeCitiesResult() {
        viewModel.getCitiesResult().observe(viewLifecycleOwner) { cities ->
            cities?.let {
                (binding.cityNameEditText as? MaterialAutoCompleteTextView)
                    ?.setSimpleItems(cities.map { it.cityName }.toTypedArray())
            }
        }
    }

    private fun observeCitiesError() {
        viewModel.getCitiesErrors().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    error,
                    getString(R.string.ok),
                ) {  }
            }
        }
    }

    private fun observePostsResult() {
        viewModel.getPostsResult().observe(viewLifecycleOwner) { posts ->
            binding.noPostsFound.isVisible = posts.isNullOrEmpty()
            posts?.let {
                postsAdapter.setPosts(it)
            }
        }
    }

    private fun observePostsError() {
        viewModel.getPostsErrors().observe(viewLifecycleOwner) { error ->
            error?.let {
                alertDialogOneOption(
                        requireContext(),
                        getString(R.string.user_alert),
                        null,
                        error,
                        getString(R.string.ok),
                ) {  }
            }
        }
    }

    private fun onPostClicked(post: GenericPostDomain) {
        navigateFromCityNameSearchOnPostType(post.postType, post.id, ::navigateSafe)
    }

    companion object {
        const val CITY_SEARCH = "citySearch"
    }
}