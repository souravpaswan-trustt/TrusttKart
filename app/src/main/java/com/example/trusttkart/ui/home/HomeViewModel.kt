package com.example.trusttkart.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.retrofit.ProductsResponse
import com.example.trusttkart.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _productList = MutableLiveData<List<ProductsResponse>?>()
    val productList: LiveData<List<ProductsResponse>?> get() = _productList

    private val _demoData = MutableLiveData<ArrayList<ArrayList<String>>>()
    val demoData: LiveData<ArrayList<ArrayList<String>>> get() = _demoData

    private val _productInCart = MutableLiveData<Boolean>()
    val productInCart: LiveData<Boolean> get() = _productInCart

    private val _addToCartLiveData = MutableLiveData<String>()
    val addToCartLiveData :LiveData<String> get() = _addToCartLiveData

    private val _userIdLiveData = MutableLiveData<Int>()
    val userIdLiveData: LiveData<Int> get() = _userIdLiveData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            RetrofitInstance.authService.getProducts()
                .enqueue(object : Callback<List<ProductsResponse>> {
                    override fun onResponse(
                        call: Call<List<ProductsResponse>>,
                        response: Response<List<ProductsResponse>>
                    ) {
                        val body = response.body()
                        if (response.isSuccessful && body != null) {
                            _productList.postValue(body)
                            _productList.value?.let { products ->
                                val demoData = ArrayList<ArrayList<String>>()
                                for (product in products) {
                                    if (product.categoryType.equals("winter", ignoreCase = true)) {
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
                                _demoData.postValue(demoData)
                            }

                        } else {
                            Log.i("Retrofit", response.errorBody().toString())
                        }
                    }

                    override fun onFailure(call: Call<List<ProductsResponse>>, t: Throwable) {
                        Log.i("Retrofit", t.message.toString())
                    }
                })
        }
    }

    suspend fun checkProductInCart(productId: Int, userId: Int){
        withContext(Dispatchers.IO) {
            var flag = false
            val cartResponse = RetrofitInstance.authService.getCart(userId).execute()
            val cartList = cartResponse.body()
            if (cartList != null && cartList.size > 0) {
                for (item in cartList) {
                    Log.i("Retrofit", item.toString())
                    if (item.product.id == productId) {
                        Log.i("Retrofit", "Product id  ${item.product.id} already in cart")
                        flag = true
                        break
                    }
                }
            }
            _productInCart.postValue(flag)
        }
    }

    fun addToCart(cartItem: CartDetails){
        Log.i("Retrofit", cartItem.toString())

        val addToCartResponse = RetrofitInstance.authService.addToCart(cartItem).execute()
        if (addToCartResponse.isSuccessful && addToCartResponse.body() != null && addToCartResponse.body()!!.success) {
            _addToCartLiveData.postValue("Added to cart")

        } else {
            _addToCartLiveData.postValue("Failed to add product to cart")
        }
        Log.i("Retrofit", addToCartResponse.body().toString())
    }

    suspend fun fetchUserId(email: String){
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.authService.getUserByEmail(email).execute()
            _userIdLiveData.postValue(response.body())
        }
    }
}