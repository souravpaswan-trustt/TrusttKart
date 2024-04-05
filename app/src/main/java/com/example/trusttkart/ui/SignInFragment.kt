package com.example.trusttkart.ui

import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentSignInBinding
import com.example.trusttkart.retrofit.LoginCredentials
import com.example.trusttkart.retrofit.LoginResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.retrofit.RetrofitService
import com.example.trusttkart.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var preferences: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.myViewModel = mainViewModel

        GlobalScope.launch(Dispatchers.IO) {
            preferences = SharedPreferencesManager.getInstance(this@SignInFragment.requireContext(),"sharedpref")
        }

        binding.SignInButton.setOnClickListener {

            val email = mainViewModel.email.value.toString().trim()
            val password = mainViewModel.password.value.toString()

            if (email == null || email == "" || password == null || password == "") {
                Toast.makeText(
                    this@SignInFragment.requireContext(),
                    "Please enter values for the fields!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val credentials = LoginCredentials(email, password)

                RetrofitInstance.authService.login(credentials).enqueue(object :
                    Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.success) {
                            val user = loginResponse.user
                            // Login successful, handle user data
                            Log.i("Retrofit", "Login successful")
                            preferences.saveLoggedInUser(email) // Save logged in user's email
                            Toast.makeText(
                                requireContext(),
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.i("Retrofit", "Login Failed")
                            Toast.makeText(
                                requireContext(),
                                "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            }
        }

        binding.signInToSignUpTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.forgotPasswordtextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }

        return binding.root
    }
}