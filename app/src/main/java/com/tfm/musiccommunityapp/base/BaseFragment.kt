package com.tfm.musiccommunityapp.base

import android.content.Context
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.tfm.musiccommunityapp.ui.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

abstract class BaseFragment (@LayoutRes layout: Int): Fragment(layout) {

    protected val baseViewModel by activityViewModel<HomeScreenViewModel>()

    private lateinit var activity: MainActivity

    private fun NavController.navigateSafe(directions: NavDirections) {
        try {
            navigate(directions)
        } catch (e: IllegalArgumentException) {
            Log.e("Navigator", "navigateSafe error $e")
        }
    }

    private fun NavController.navigateSafe(@IdRes resId: Int) {
        try {
            navigate(resId, null)
        } catch (e: IllegalArgumentException) {
            Log.e("Navigator", "navigateSafe error $e")
        }
    }

    fun showLoader() {
        activity.showLoader()
    }

    fun hideLoader() {
        activity.hideLoader()
    }

    fun navigateSafe(directions: NavDirections) {
        view?.post { findNavController().navigateSafe(directions) }
    }

    fun navigateSafe(@IdRes resId: Int) {
        view?.post { findNavController().navigateSafe(resId) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

}
