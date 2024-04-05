package com.example.trusttkart.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }

    val name = MutableLiveData<String>()
}