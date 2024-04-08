package com.example.trusttkart.retrofit


import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.data.LoginCredentials
import com.example.trusttkart.data.RegisterCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface RetrofitService {

    @GET("/api/user/email/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<Int>

    @POST("api/login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>

    @POST("api/register")
    fun register(@Body credentials: RegisterCredentials): Call<RegisterResponse>

    @GET("product/")
    fun getProducts(): Call<List<ProductsResponse>>

    @POST("/cart/add")
    fun addToCart(@Body cartDetails: CartDetails): Call<AddToCartResponse>

    @GET("/cart/{userId}")
    fun getCart(@Path("userId") userId: Int): Call<List<FetchCartResponse>>
}