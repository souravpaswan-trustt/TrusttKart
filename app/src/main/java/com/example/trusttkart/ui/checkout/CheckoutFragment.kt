package com.example.trusttkart.ui.checkout

import SharedPreferencesManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentCheckoutBinding
import com.example.trusttkart.ui.home.HomeViewModel
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var preferences: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkoutViewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        binding.myViewModel = checkoutViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SharedPreferencesManager.getInstance(requireContext(), "sharedpref")
        val email = preferences.getLoggedInUser()
        lifecycleScope.launch {
            checkoutViewModel.fetchUserId(email!!)
            val userId = checkoutViewModel.userIdLiveData.value
            checkoutViewModel.getUserData(userId!!)
            if(checkoutViewModel.errorLiveData.value != null) {
                Toast.makeText(requireContext(), checkoutViewModel.errorLiveData.value, Toast.LENGTH_SHORT).show()
            } else{
                val userData = checkoutViewModel.userDataLiveData.value
//                binding.name.text = userData?.user?.name
//                binding.email.text = userData?.user?.email
//                binding.phone.text = userData?.user?.phone
            }
        }
    }
}