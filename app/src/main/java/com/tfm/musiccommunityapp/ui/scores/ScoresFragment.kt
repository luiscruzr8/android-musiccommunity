package com.tfm.musiccommunityapp.ui.scores

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.ScoresFragmentBinding
import com.tfm.musiccommunityapp.domain.model.ScoreDomain
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScoresFragment: BaseFragment(R.layout.scores_fragment) {

    private val binding by viewBinding(ScoresFragmentBinding::bind)
    private val viewModel by viewModel<ScoresViewModel>()
    private val scoresAdapter = ScoresAdapter(::onScoreClicked)

    private var searchTerm = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SEARCH_TERM, binding.searchEditText.text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUpScores(searchTerm)

        observeLoader()
        observeScoresResult()
        observeScoresError()


        //viewModel.setUpScores(getCurrentUserLogin(), null)

        binding.rvMyScores.apply {
            layoutManager = GridLayoutManager(
                context,
                1//2
            )
            adapter = scoresAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
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
        Toast.makeText(requireContext(), "Score clicked ${score.id}", Toast.LENGTH_SHORT).show()
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
                    // TODO: Handle the selected PDF file
                    Toast.makeText(
                        requireContext(),
                        "Selected pdf ${uri.path}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    companion object {
        const val SEARCH_TERM = "searchTerm"
    }

}