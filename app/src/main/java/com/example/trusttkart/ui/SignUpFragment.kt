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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentSignUpBinding
import com.example.trusttkart.retrofit.LoginCredentials
import com.example.trusttkart.retrofit.LoginResponse
import com.example.trusttkart.retrofit.RegisterCredentials
import com.example.trusttkart.retrofit.RegisterResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.myViewModel = mainViewModel

        val mSpannableString = SpannableString("Login")
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        binding.loginTextView.text = mSpannableString

        binding.SignUpButton.setOnClickListener {
            try {

                val name = mainViewModel.fullname.value.toString().trim()
                val email = mainViewModel.email.value.toString().trim()
                val phone = mainViewModel.phoneNumber.value.toString().trim()
                val password = mainViewModel.password.value
                val retypePass = mainViewModel.retypePassword.value

                if (name == null || name == ""|| password == null || password=="" || email == null ||
                    email == "" || phone == null || phone == "" || retypePass == "" || retypePass == null){
                    Toast.makeText(this@SignUpFragment.requireContext(),
                        "Please enter values for the fields!",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    try {
                        val credentials = RegisterCredentials(name, email, phone, password)

                        RetrofitInstance.authService.register(credentials).enqueue(object :
                            Callback<RegisterResponse> {
                            override fun onResponse(
                                call: Call<RegisterResponse>,
                                response: Response<RegisterResponse>
                            ) {
                                val registerResponse = response.body()
                                if (registerResponse != null && registerResponse.success) {
                                    val user = registerResponse.user
                                    // Registration successful, handle user data
                                    Log.i("Retrofit", "Sign up successful")
                                    val intent = Intent(requireContext(), MainActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign up successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Log.i("Retrofit", "Sign up Failed")
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign up failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                Toast.makeText(
                                    requireContext(),
                                    "${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

//                val intent = Intent(requireContext(), MainActivity::class.java)
//                startActivity(intent)
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