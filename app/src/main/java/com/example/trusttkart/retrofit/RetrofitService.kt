package com.example.trusttkart.retrofit


import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.data.LoginCredentials
import com.example.trusttkart.data.RegisterCredentials
import com.example.trusttkart.data.UpdateCartCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @DELETE("/cart/{userId}/{pId}")
    fun deleteCartItem(@Path("userId") userId: Int, @Path("pId") pId: Int): Call<DeleteCartItemResponse>

    @PUT("/cart")
    fun updateCartItem(@Body credentials: UpdateCartCredentials): Call<UpdateCartItemResponse>

    @GET("api/user/{userId}")
    fun getUserData(@Path("userId") userId: Int): Call<UserDataResponse>

}