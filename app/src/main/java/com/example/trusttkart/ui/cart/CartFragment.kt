package com.example.trusttkart.ui.cart

import SharedPreferencesManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentCartBinding
import com.example.trusttkart.recyclerview.CartRVAdapter
import com.example.trusttkart.retrofit.FetchCartResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var preferences: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.myViewModel = cartViewModel
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                //        binding.cartDetailsRecyclerView.setBackgroundColor(Color.BLUE)
                binding.cartDetailsRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.cartDetailsRecyclerView.adapter = CartRVAdapter(getCartItems())
            }
        }
        return binding.root
    }

    private suspend fun getCartItems(): List<FetchCartResponse> {
        preferences = SharedPreferencesManager.getInstance(requireContext(), "shared_pref")
        val cartItems = arrayListOf<FetchCartResponse>()
        withContext(Dispatchers.IO) {
            try {
                val userEmail = preferences.getLoggedInUser()
                if (userEmail != null) {
                    val response = RetrofitInstance.authService.getUserByEmail(userEmail).execute()
                    val userId = response.body()
                    if (userId != null) {
                        val cartResponse = RetrofitInstance.authService.getCart(userId).execute()
                        val cartList = cartResponse.body()
                        if (cartList != null && cartList.size > 0) {
                            for (item in cartList) {
                                cartItems.add(item)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.i("Retrofit", e.message.toString())
                    Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return cartItems
    }
}