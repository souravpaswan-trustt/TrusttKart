package com.example.trusttkart.ui.home

import SharedPreferencesManager
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.trusttkart.AuthActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentHomeBinding
import com.example.trusttkart.recyclerview.CarouselRVAdapter
import com.example.trusttkart.retrofit.ProductsResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        lifecycleScope.launch {
            val demoData = getData()
            withContext(Dispatchers.Main) {
                binding.viewPager.adapter = CarouselRVAdapter(demoData)

                val compositePageTransformer = CompositePageTransformer()
                compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
                binding.viewPager.setPageTransformer(compositePageTransformer)
            }
        }

        return binding.root
    }

    private suspend fun getData() : ArrayList<ArrayList<String>>{
        val demoData = ArrayList<ArrayList<String>>()
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.authService.getProducts().execute()
                val productList = response.body()
                if (productList != null) {
                    for (product in productList) {
                        if(product.categoryType.equals("winter", ignoreCase = true)) {
                            Log.i("Retrofit", product.productName)
                            val productData = arrayListOf(
                                product.productName,
                                product.categoryType,
                                "₹" + product.productPrice.toString(),
                                product.imageUrl,
                                product.id.toString()
                            )
                            demoData.add(productData)
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        return demoData
    }
}