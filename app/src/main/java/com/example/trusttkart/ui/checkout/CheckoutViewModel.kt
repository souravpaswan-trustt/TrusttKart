package com.example.trusttkart.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.retrofit.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel: ViewModel() {

    private val _userIdLiveData = MutableLiveData<Int?>()
    val userIdLiveData: LiveData<Int?> get() = _userIdLiveData

    private val  _userDataLiveData = MutableLiveData<UserDataResponse?>()
    val userDataLiveData: LiveData<UserDataResponse?> get() = _userDataLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    suspend fun fetchUserId(email: String){
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.authService.getUserByEmail(email).execute()
            _userIdLiveData.postValue(response.body())
        }
    }

    suspend fun getUserData(userId: Int){
        withContext(Dispatchers.IO) {
            RetrofitInstance.authService.getUserData(userId).enqueue(object :
            Callback<UserDataResponse> {
                override fun onResponse(
                    call: Call<UserDataResponse>,
                    response: Response<UserDataResponse>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        _userDataLiveData.postValue(body)
                    } else {
                        _errorLiveData.postValue(response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                    _errorLiveData.postValue(t.message)
                }
            })
        }
    }
}