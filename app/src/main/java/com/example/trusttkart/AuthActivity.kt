package com.example.trusttkart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.trusttkart.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        try {
            val fragmentName = intent.getStringExtra("fragmentName")
            when (fragmentName) {
                "SignInFragment" -> {
                    Log.i("trial", "Hello")
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    val navController = navHostFragment.navController
                    navController.navigate(R.id.signInFragment)
                }
                else -> {

                }
            }
        } catch(e : Exception){
            Log.i("IntentError", e.toString())
        }
    }
}