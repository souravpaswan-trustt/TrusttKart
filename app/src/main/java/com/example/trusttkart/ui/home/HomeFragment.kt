package com.example.trusttkart.ui.home

import SharedPreferencesManager
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.trusttkart.AuthActivity
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.databinding.FragmentHomeBinding
import com.example.trusttkart.recyclerview.CarouselRVAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var preferences: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.myViewModel = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SharedPreferencesManager.getInstance(requireContext(), "sharedpref")

        val loggedInUser = preferences.getLoggedInUser()
        if (loggedInUser != null) {
            binding.signInSignUpLayout.visibility = View.GONE
        } else {
            // No user is logged in
            binding.signInSignUpLayout.visibility = View.VISIBLE
        }

        binding.homeFragmentToSignIn.setOnClickListener {
            try {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra("fragmentName", "SignInFragment")
                startActivity(intent)
            } catch (e: Exception) {
                Log.i("IntentError", e.toString())
            }
        }

        binding.homeFragmentToSignUp.setOnClickListener {
            try {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra("fragmentName", "SignUpFragment")
                startActivity(intent)
            } catch (e: Exception) {
                Log.i("IntentError", e.toString())
            }
        }

        binding.viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }

//        homeViewModel.getData()
        homeViewModel.demoData.observe(viewLifecycleOwner, Observer { data ->
            binding.viewPager.adapter = CarouselRVAdapter(
                data,
                object : CarouselRVAdapter.CarouselAdapterItemClickListener {
                    override fun onAddToCartClickListener(holder: CarouselRVAdapter.CarouselItemViewHolder, productId: Int) {
                        val email = preferences.getLoggedInUser()
                        if(email.isNullOrEmpty()){
                            onAddToCartClickWithoutSignIn()
                        }
                        else{
                            lifecycleScope.launch {
                                homeViewModel.fetchUserId(email)
                                val userId = homeViewModel.userIdLiveData.value
                                onAddToCartClickWithSignIn(holder, productId, userId!!)
                            }
                        }
                    }

                    override fun carouselHomeToCartFragment() {
                        (requireActivity() as MainActivity).binding.bottomNavigation.selectedItemId = R.id.navigation_cart
                    }
                })

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
            binding.viewPager.setPageTransformer(compositePageTransformer)
        })
        homeViewModel.getData()
    }

    fun onAddToCartClickWithoutSignIn() {
        CoroutineScope(Dispatchers.Main).launch{
            Toast.makeText(requireContext(), "Please login", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    fun onAddToCartClickWithSignIn(holder: CarouselRVAdapter.CarouselItemViewHolder, productId: Int, userId: Int ) {
        lifecycleScope.launch(Dispatchers.IO) {
            homeViewModel.checkProductInCart(productId, userId)
            Log.i("Retrofit", "Product in cart ${homeViewModel.productInCart.value.toString()}")
            if (homeViewModel.productInCart.value == false) {
                val cartItem = CartDetails(userId, productId, 1)
                homeViewModel.addToCart(cartItem)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), homeViewModel.addToCartLiveData.value, Toast.LENGTH_SHORT).show()
                }
            }
            withContext(Dispatchers.Main) {
                holder.addToCartButton.text = "Go to cart"
                holder.addToCartButton.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.blueTheme
                    )
                )
                holder.addToCartButton.backgroundTintList =
                    ColorStateList.valueOf(Color.WHITE)
            }
        }
    }
}