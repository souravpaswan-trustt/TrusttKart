package com.example.trusttkart.ui.profile

import SharedPreferencesManager
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentProfileBinding
import com.example.trusttkart.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var preferences: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.myViewModel = profileViewModel
        binding.lifecycleOwner = this

        GlobalScope.launch(Dispatchers.IO) {
            preferences = SharedPreferencesManager.getInstance(this@ProfileFragment.requireContext(),"sharedpref")
        }

        binding.logoutButton.setOnClickListener {
            preferences.logoutUser()
            it.findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
            Toast.makeText(this.requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}