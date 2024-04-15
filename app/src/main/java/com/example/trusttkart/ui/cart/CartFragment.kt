package com.example.trusttkart.ui.cart

import SharedPreferencesManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trusttkart.R
import com.example.trusttkart.data.UpdateCartCredentials
import com.example.trusttkart.databinding.FragmentCartBinding
import com.example.trusttkart.recyclerview.CartRVAdapter
import com.example.trusttkart.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            preferences = SharedPreferencesManager.getInstance(requireContext(), "shared_pref")
            val email = preferences.getLoggedInUser()
            cartViewModel.getCartItems(email)
            cartViewModel.fetchUserId(email!!)
            cartViewModel.cartList.observe(viewLifecycleOwner, Observer { cartItems ->
                if (cartItems != null) {
                    binding.cartDetailsRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.cartDetailsRecyclerView.adapter = CartRVAdapter(
                        object : CartRVAdapter.CartItemOnClickListener {
                            override fun removeCartItemOnClick(productId: Int) {
                                lifecycleScope.launch {
                                    val userId = cartViewModel.userIdLiveData.value
                                    cartViewModel.deleteCart(userId!!, productId)
                                }
                            }

                            override fun updateCartItemOnClick(productId: Int, increase: Boolean) {
                                lifecycleScope.launch {
                                    val userId = cartViewModel.userIdLiveData.value
                                    cartViewModel.cartList.value?.forEach {
                                        if (it.product.id == productId) {
                                            val quantity =
                                                if (increase) it.quantity + 1 else it.quantity - 1
                                            cartViewModel.updateCartItems(
                                                userId!!,
                                                productId,
                                                quantity
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        cartItems
                    )
                } else {
                    Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.proceedToBuyButton.setOnClickListener {
            it.findNavController().navigate(R.id.checkoutFragment)
        }
    }
}