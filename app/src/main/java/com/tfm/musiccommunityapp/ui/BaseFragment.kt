package com.tfm.musiccommunityapp.ui

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.tfm.musiccommunityapp.ui.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment (@LayoutRes layout: Int): Fragment(layout) {

    protected val baseViewModel by sharedViewModel<HomeScreenViewModel>()

    fun NavController.navigateSafe(directions: NavDirections) {
        try {
            navigate(directions)
        } catch (e: Exception) {
            Log.e("Navigator", "navigateSafe error $e")
        }
    }

}
