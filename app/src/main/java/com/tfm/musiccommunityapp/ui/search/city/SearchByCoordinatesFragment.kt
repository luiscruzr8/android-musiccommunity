package com.tfm.musiccommunityapp.ui.search.city

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.tfm.musiccommunityapp.R
import com.tfm.musiccommunityapp.databinding.SearchByCoordinatesFragmentBinding
import com.tfm.musiccommunityapp.domain.model.GenericPostDomain
import com.tfm.musiccommunityapp.ui.base.BaseFragment
import com.tfm.musiccommunityapp.ui.dialogs.common.alertDialogOneOption
import com.tfm.musiccommunityapp.ui.profile.posts.UserPostsAdapter
import com.tfm.musiccommunityapp.ui.utils.navigateFromCoordinatesSearchOnPostType
import com.tfm.musiccommunityapp.ui.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchByCoordinatesFragment : BaseFragment(R.layout.search_by_coordinates_fragment) {

    private val binding by viewBinding(SearchByCoordinatesFragmentBinding::bind)
    private val viewModel by viewModel<SearchByCityViewModel>()
    private val postsAdapter = UserPostsAdapter(::onPostClicked)

    private var searchOnClosest = false
    private var latitude = 0.0
    private var longitude = 0.0

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SEARCH_CLOSEST, searchOnClosest)
        outState.putDouble(SEARCH_CLOSEST, latitude)
        outState.putDouble(SEARCH_CLOSEST, longitude)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            searchOnClosest = savedInstanceState.getBoolean(SEARCH_CLOSEST, false)
            latitude = savedInstanceState.getDouble(LATITUDE, 0.0)
            longitude = savedInstanceState.getDouble(LONGITUDE, 0.0)
            binding.searchClosestCheckBox.isChecked = searchOnClosest
            binding.tvLatitude.text = getString(R.string.latitude, latitude)
            binding.tvLongitude.text = getString(R.string.longitude, longitude)
        }

        observeLoader()
        observePostsResult()
        observePostsError()

        getLastKnownLocation()
        binding.rvPosts.apply {
            adapter = postsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        binding.submitSearchButton.setOnClickListener {
            viewModel.setUpSearchByCoordinates(latitude, longitude, searchOnClosest)
        }

    }

    private fun observeLoader() {
        viewModel.isLoading().observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else hideLoader()
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
        navigateFromCoordinatesSearchOnPostType(post.postType, post.id, ::navigateSafe)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                alertDialogOneOption(
                    requireContext(),
                    getString(R.string.user_alert),
                    null,
                    "You need to provide access to your location to use this feature.\n You will be redirected to the previous screen.",
                    getString(R.string.ok),
                ) { findNavController().popBackStack() }
            }
        }

    private fun getLastKnownLocation() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permissions
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val latitude = location?.latitude
        val longitude = location?.longitude

        // Use the coordinates
        if (latitude != null && longitude != null) {
            this.latitude = latitude
            this.longitude = longitude
            binding.tvLatitude.text = getString(R.string.latitude, latitude)
            binding.tvLongitude.text = getString(R.string.longitude, longitude)
            viewModel.setUpSearchByCoordinates(latitude, longitude, searchOnClosest)
        } else {
            alertDialogOneOption(
                requireContext(),
                getString(R.string.user_alert),
                null,
                "Error getting your location, please try again later.",
                getString(R.string.ok),
            ) { findNavController().popBackStack() }
        }
    }


    companion object {
        private const val SEARCH_CLOSEST = "searchClosest"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
    }
}