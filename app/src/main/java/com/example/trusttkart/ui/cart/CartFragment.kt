package com.example.trusttkart.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentCartBinding
import com.example.trusttkart.ui.home.HomeViewModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel

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
}