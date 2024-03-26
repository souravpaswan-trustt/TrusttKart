package com.example.trusttkart.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        binding.SignInButton.setOnClickListener {
            try {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } catch(e: Exception){
                Log.i("IntentError", e.toString())
            }
        }

        binding.signInToSignUpTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        return binding.root
    }
}