package com.example.trusttkart.ui.home

import SharedPreferencesManager
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.trusttkart.AuthActivity
import com.example.trusttkart.R
import com.example.trusttkart.databinding.FragmentHomeBinding
import com.example.trusttkart.recyclerview.CarouselRVAdapter

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

         val demoData = arrayListOf(
            "Christmas Tree",
            "Armchair and footstool",
            "Rocking-chair",
            "Smartphones",
            "Laptops and Desktops"
        )

        binding.viewPager.adapter = CarouselRVAdapter(demoData)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        binding.viewPager.setPageTransformer(compositePageTransformer)

        return binding.root
    }
}