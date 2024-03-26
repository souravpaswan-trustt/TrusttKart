package com.example.trusttkart.ui.home

import android.R.attr.fragment
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.myViewModel = homeViewModel
        binding.lifecycleOwner = this

        binding.homeFragmentToSignIn.setOnClickListener {
            try {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra("fragmentName", "SignInFragment")
                startActivity(intent)
            } catch(e: Exception){
                Log.i("IntentError", e.toString())
            }
        }

        binding.homeFragmentToSignUp.setOnClickListener {
            try {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.putExtra("fragmentName", "SignUpFragment")
                startActivity(intent)
            } catch(e: Exception){
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
            "Curabitur sit amet rutrum enim, sit amet commodo urna. Nullam nec nisl eget purus vulputate ultrices nec sit amet est. Sed sodales maximus risus sit amet placerat.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus sit amet lectus a mi lobortis iaculis. Mauris odio tortor, accumsan vel gravida sit amet, malesuada a tortor.",
            "Praesent efficitur eleifend eros quis elementum. Vivamus eget nunc ante. Sed sed sodales libero. Nam ipsum lorem, consequat at ipsum sit amet, tempor vulputate nibh.",
            "Aliquam sodales imperdiet augue at consectetur. Suspendisse dui mauris, tincidunt non auctor quis, facilisis et tellus.",
            "Ut non tincidunt neque, et sodales ligula. Quisque interdum in dui sit amet sagittis. Curabitur erat magna, maximus quis libero quis, dapibus eleifend orci."
        )

        binding.viewPager.adapter = CarouselRVAdapter(demoData)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        binding.viewPager.setPageTransformer(compositePageTransformer)

        return binding.root
    }
}