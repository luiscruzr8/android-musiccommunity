package com.tfm.musiccommunityapp.ui.search.tag

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.SearchPostsByTagFragmentBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.posts.UserPostsAdapter
import com.tfm.musiccommunityapp.ui.utils.navigateFromTagSearchOnPostType
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchPostsByTagFragment: BaseFragment(R.layout.search_posts_by_tag_fragment) {

    private val binding by viewBinding(SearchPostsByTagFragmentBinding::bind)
    private val viewModel by viewModel<SearchByTagViewModel>()
    private val postsAdapter = UserPostsAdapter(::onPostClicked)

    private var tagNameSearch = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(TAG_NAME_SEARCH, tagNameSearch)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            tagNameSearch = savedInstanceState.getCharSequence(TAG_NAME_SEARCH, "").toString()
            binding.tagNameEditText.setText(tagNameSearch)
        }

        observeLoader()
        observeTagsResult()
        observeTagsError()
        observePostsResult()
        observePostsError()

        viewModel.setUpSearchByTag(tagNameSearch)
        binding.rvPosts.apply {
            adapter = postsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        (binding.tagNameEditText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener  { parent, _, position, _ ->
                tagNameSearch = parent.getItemAtPosition(position) as String
            }

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpSearchByTag(tagNameSearch)
        }
    }

    private fun observeLoader() {
        viewModel.isLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeTagsResult() {
        viewModel.getTagsOrInterestsResult().observe(viewLifecycleOwner) { tags ->
            tags?.let {
                (binding.tagNameEditText as? MaterialAutoCompleteTextView)
                    ?.setSimpleItems(tags.map { it.tagName }.toTypedArray())
            }
        }
    }

    private fun observeTagsError() {
        viewModel.getTagsOrInterestsErrors().observe(viewLifecycleOwner) { error ->
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
        navigateFromTagSearchOnPostType(post.postType, post.id, ::navigateSafe)
    }

    companion object {
        const val TAG_NAME_SEARCH = "tagNameSearch"
    }
}