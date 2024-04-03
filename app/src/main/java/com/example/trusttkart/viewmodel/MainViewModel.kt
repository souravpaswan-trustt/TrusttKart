package com.example.trusttkart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val fullname = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val retypePassword = MutableLiveData<String>()
}