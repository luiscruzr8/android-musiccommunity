package com.tfm.musiccommunityapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tfm.musiccommunityapp.databinding.MainActivityBinding
import com.tfm.musiccommunityapp.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)

    override fun onStart() {
        super.onStart()
        binding.navHostFragment.findNavController()
    }
}