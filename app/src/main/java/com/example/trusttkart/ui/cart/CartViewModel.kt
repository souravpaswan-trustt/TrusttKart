package com.example.trusttkart.ui.cart

import SharedPreferencesManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trusttkart.data.UpdateCartCredentials
import com.example.trusttkart.retrofit.DeleteCartItemResponse
import com.example.trusttkart.retrofit.FetchCartResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import com.example.trusttkart.retrofit.UpdateCartItemResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val _cartList = MutableLiveData<List<FetchCartResponse>?>()
    val cartList: LiveData<List<FetchCartResponse>?> get() = _cartList

    private val _toastResponse = MutableLiveData<String>()
    val toastResponse :LiveData<String> get() = _toastResponse

    private val _userIdLiveData = MutableLiveData<Int>()
    val userIdLiveData: LiveData<Int> get() = _userIdLiveData

    var email = ""
    suspend fun getCartItems(userEmail: String?){
        email = userEmail!!
        withContext(Dispatchers.IO) {
            try {
                if (userEmail != null) {
                    val response = RetrofitInstance.authService.getUserByEmail(userEmail).execute()
                    if(response.body() != null){
                        val userId = response.body()!!
                        Log.i("Preferences", "User ID: $userId")
                        val cartResponse = RetrofitInstance.authService.getCart(userId).execute()
                        val cartResponseBody = cartResponse.body()
                        if (!cartResponseBody.isNullOrEmpty()) {
                            _cartList.postValue(cartResponseBody)
                        } else {
                            _toastResponse.postValue("Cart is empty")
                        }
                    } else {
                        _toastResponse.postValue("User not found")
                    }
                } else {
                    _toastResponse.postValue("User not logged in")
                }
            } catch (e: Exception) {
                Log.i("Retrofit", e.message.toString())
                _toastResponse.postValue(e.message)
            }
        }
    }

    fun deleteCart(userId: Int, pId: Int){
        try{
            RetrofitInstance.authService.deleteCartItem(userId, pId).enqueue(object :
                Callback<DeleteCartItemResponse>{
                override fun onResponse(
                    call: Call<DeleteCartItemResponse>,
                    response: Response<DeleteCartItemResponse>
                ) {
                    if(response.isSuccessful){
                        Log.i("Retrofit", "Item deleted successfully")
                        viewModelScope.launch {
                            getCartItems(email) // Refresh the cart items
                        }
                    } else{
                        Log.i("Retrofit", "Item could not be deleted")
                    }
                }

                override fun onFailure(call: Call<DeleteCartItemResponse>, t: Throwable) {
                    Log.i("Retrofit", "Something went wrong")
                }
            })
        } catch(e: Exception){
            Log.i("Retrofit", e.message.toString())
        }
    }

    fun updateCartItems(userId: Int, pId: Int, quantity: Int){
        try{
            if(quantity == 0){
                deleteCart(userId, pId)
            } else {
                val credentials = UpdateCartCredentials(userId, pId, quantity)
                RetrofitInstance.authService.updateCartItem(credentials).enqueue(object :
                    Callback<UpdateCartItemResponse> {

                    override fun onResponse(
                        call: Call<UpdateCartItemResponse>,
                        response: Response<UpdateCartItemResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.i("Retrofit", "Item updated successfully")
                            viewModelScope.launch {
                                getCartItems(email) // Refresh the cart items
                            }
                        } else {
                            Log.i("Retrofit", "Item could not be updated")
                        }
                    }

                    override fun onFailure(call: Call<UpdateCartItemResponse>, t: Throwable) {
                        Log.i("Retrofit", "Something went wrong")
                    }
                })
            }
        } catch(e: Exception){
            Log.i("Retrofit", e.message.toString())
        }
    }

    suspend fun fetchUserId(email: String){
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.authService.getUserByEmail(email).execute()
            _userIdLiveData.postValue(response.body())
        }
    }
}