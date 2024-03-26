package com.example.trusttkart.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        val mSpannableString = SpannableString("Login")
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        binding.loginTextView.text = mSpannableString

        binding.SignUpButton.setOnClickListener {
            try {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } catch(e: Exception){
                Log.i("IntentError", e.toString())
            }
        }

        binding.loginTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return binding.root
    }
}