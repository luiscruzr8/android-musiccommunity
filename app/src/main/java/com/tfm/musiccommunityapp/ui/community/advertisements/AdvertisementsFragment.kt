package com.tfm.musiccommunityapp.ui.community.advertisements

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.base.BaseFragment
import com.tfm.musiccommunityapp.databinding.AdvertisementsFragmentBinding
import com.tfm.musiccommunityapp.domain.model.AdvertisementDomain
import com.tfm.musiccommunityapp.ui.community.CommunityFragmentDirections
import com.tfm.musiccommunityapp.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdvertisementsFragment: BaseFragment(R.layout.advertisements_fragment) {

    private val binding by viewBinding(AdvertisementsFragmentBinding::bind)
    private val viewModel by viewModel<AdvertisementsViewModel>()
    private val advertisementsAdapter = AdvertisementsAdapter(::onAdvertisementClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoader()
        observeAdvertisementsResult()
        observeAdvertisementsError()

        binding.rvCommunityAdvertisements.apply {
            adapter = advertisementsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    fun restartViewPager(searchTerm: String?) {
        viewModel.setUpAdvertisements(if (searchTerm.isNullOrBlank()) null else searchTerm)
    }

    private fun observeLoader() {
        viewModel.isAdvertisementsLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
        }
    }

    private fun observeAdvertisementsResult() {
        viewModel.getAdvertisementsLiveData().observe(viewLifecycleOwner) { advertisements ->
            advertisementsAdapter.setAdvertisements(advertisements)
            binding.noAdvertisementsFound.visibility =
                if (advertisements.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun onAdvertisementClicked(advertisement: AdvertisementDomain) {
        val action =
            CommunityFragmentDirections.actionCommunityFragmentToAdvertisementDetailFragment(
                advertisement.id
            )
        navigateSafe(action)
    }

    private fun observeAdvertisementsError() {
        viewModel.getCommunityAdvertisementsError().observe(viewLifecycleOwner) { error ->
            binding.noAdvertisementsFound.text = error
        }
    }
}