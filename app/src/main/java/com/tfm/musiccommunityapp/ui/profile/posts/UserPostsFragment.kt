package com.tfm.musiccommunityapp.ui.profile.posts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.UserPostsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.ProfileViewModel
import com.tfm.musiccommunityapp.utils.getPostType
import com.tfm.musiccommunityapp.utils.navigateOnPostType
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserPostsFragment: BaseFragment(R.layout.user_posts_fragment) {

    private val binding by viewBinding(UserPostsFragmentBinding::bind)
    private val viewModel by viewModel<ProfileViewModel>()
    private val args by navArgs<UserPostsFragmentArgs>()
    private val postsAdapter = UserPostsAdapter(::onPostClicked)

    private var searchTerm = ""
    private var postType = R.string.all

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_TERM, binding.searchEditText.text)
        outState.putInt(POST_TYPE, postType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.postTypeEditText.setText(postType)

        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getCharSequence(SEARCH_TERM, "").toString()
            postType = savedInstanceState.getInt(POST_TYPE, R.string.all)
            binding.searchEditText.setText(searchTerm)
            binding.postTypeEditText.setText(postType)
        }

        observeLoader()
        observePostsResult()
        observePostsError()

        viewModel.setUpPosts(
            args.login,
            getPostType(postType),
            searchTerm
        )

        binding.rvMyPosts.apply {
            adapter = postsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchTerm = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val types = arrayOf(
            R.string.all to getString(R.string.all),
            R.string.advertisement to getString(R.string.advertisement),
            R.string.discussion to getString(R.string.discussion),
            R.string.event to getString(R.string.event),
            R.string.opinion to getString(R.string.opinion)
        )

        (binding.postTypeEditText as? MaterialAutoCompleteTextView)?.setSimpleItems(types.map { it.second }.toTypedArray())
        (binding.postTypeEditText as? MaterialAutoCompleteTextView)
            ?.setOnItemClickListener  { parent, _, position, _ ->
                val selected = parent.getItemAtPosition(position) as String
                postType = types.first { it.second == selected }.first
            }

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpPosts(
                args.login,
                getPostType(postType),
                searchTerm
            )
        }
    }

    private fun observeLoader() {
        viewModel.getShowLoader().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observePostsResult() {
        viewModel.getUserPosts().observe(viewLifecycleOwner) { posts ->
            binding.noPostsFound.isVisible = posts.isNullOrEmpty()
            posts?.let {
                postsAdapter.setPosts(it)
            }
        }
    }

    private fun observePostsError() {
        viewModel.getUserPostsError().observe(viewLifecycleOwner) { error ->
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
        navigateOnPostType(post.postType, post.id, ::navigateSafe)
    }

    companion object {
        const val SEARCH_TERM = "searchTerm"
        const val POST_TYPE = "postType"
    }
}
