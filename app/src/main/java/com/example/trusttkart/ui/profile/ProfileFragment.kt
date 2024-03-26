package com.example.trusttkart.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentProfileBinding
import com.example.trusttkart.ui.home.HomeViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.myViewModel = profileViewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}