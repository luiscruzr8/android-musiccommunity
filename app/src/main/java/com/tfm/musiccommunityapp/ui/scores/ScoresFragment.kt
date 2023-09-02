package com.tfm.musiccommunityapp.ui.scores

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ScoresFragmentBinding
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.utils.uriToFile
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoresFragment: BaseFragment(R.layout.scores_fragment) {

    private val binding by viewBinding(ScoresFragmentBinding::bind)
    private val viewModel by viewModel<ScoresViewModel>()
    private val scoresAdapter = ScoresAdapter(::onScoreClicked)
    private val args by navArgs<ScoresFragmentArgs>()
    private var searchTerm = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_TERM, binding.searchEditText.text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            searchTerm = savedInstanceState.getCharSequence(SEARCH_TERM, "").toString()
            binding.searchEditText.setText(searchTerm)
        }

        viewModel.setUpScores(searchTerm, args.login)

        observeLoader()
        observeScoresResult()
        observeScoresError()
        observeOperationSuccess()
        observeUploadError()

        setLayout(args.login)
    }

    private fun setLayout(username: String? = null) {
        binding.rvMyScores.apply {
            layoutManager = GridLayoutManager(
                context,
                1//2 seems to be really tight
            )
            adapter = scoresAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        username?.let {
            binding.fabAddItem.isVisible = false
            binding.tvScoresTitle.text = getString(R.string.scores_screen_title_user, username)
        } ?: run {
            binding.fabAddItem.isVisible = true
            binding.tvScoresTitle.text = getString(R.string.scores_screen_title_mine)
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchTerm = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.submitSearchButton.setOnClickListener {
            searchTerm = binding.searchEditText.text.toString()
            viewModel.setUpScores(searchTerm, args.login)
        }

        binding.fabAddItem.setOnClickListener {
            openFilePicker()
        }
    }

    private fun observeLoader() {
        viewModel.isScoresLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeScoresResult() {
        viewModel.getScoresLiveData().observe(viewLifecycleOwner) { scores ->
            scoresAdapter.setScores(scores)
            binding.noScoresFound.visibility = if (scores.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun observeScoresError() {
        viewModel.getScoresError().observe(viewLifecycleOwner) { error ->
            binding.noScoresFound.text = error
        }
    }

    private fun onScoreClicked(score: ScoreDomain) {
        val action = ScoresFragmentDirections.actionScoresFragmentToScoreDetailFragment(score.id)
        navigateSafe(action)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        filePickerLauncher.launch(intent)
    }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    viewModel.sendUploadScore(uriToFile(uri, requireContext()))
                }
            }
        }

    private fun observeOperationSuccess() {
        viewModel.isOperationSuccessfulLiveData().observe(viewLifecycleOwner) {
            it?.let { type ->
                when (type) {
                    ScoresViewModel.ScoreOperationSuccess.UPLOAD -> {
                        viewModel.setUpScores(searchTerm, args.login)
                        Toast.makeText(
                            requireContext(),
                            "Score uploaded",
                            Toast.LENGTH_SHORT
                        ).show() //TODO: Redirect to newly created?
                    }

                    ScoresViewModel.ScoreOperationSuccess.DELETE ->
                        viewModel.setUpScores(searchTerm, args.login)
                }
            }
        }
    }

    private fun observeUploadError() {
        viewModel.getScoreUploadError().observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val SEARCH_TERM = "searchTerm"
    }

}