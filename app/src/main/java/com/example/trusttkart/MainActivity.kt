package com.example.trusttkart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.trusttkart.databinding.ActivityMainBinding
import com.example.trusttkart.retrofit.RetrofitService
import com.example.trusttkart.retrofit.ProductsResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.ui.cart.CartFragment
import com.example.trusttkart.ui.home.HomeFragment
import com.example.trusttkart.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

//public var arrayList = mutableListOf<String>()

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        navView.setupWithNavController(navController)


//        binding.bottomNavigation.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.home_option->{
//                    findNavController(R.id.)
//                }
//            }
//        }

    }
}