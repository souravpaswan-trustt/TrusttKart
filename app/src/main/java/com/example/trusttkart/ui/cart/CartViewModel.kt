package com.example.trusttkart.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {

    val text = MutableLiveData<String>().apply {
        value = "This is Cart Fragment"
    }
}