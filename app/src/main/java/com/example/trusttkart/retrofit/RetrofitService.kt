package com.example.trusttkart.retrofit


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface RetrofitService {

//    @GET("product/")
//    fun getProducts() : Response<ProductsResponse>

    @POST("api/login")
    fun login(@Body credentials: LoginCredentials): Call<LoginResponse>

    @POST("api/register")
    fun register(@Body credentials: RegisterCredentials): Call<RegisterResponse>

    @GET("product/")
    fun getProducts(): Call<List<ProductsResponse>>
}

data class LoginCredentials(
    val email: String,
    val password: String
)

data class RegisterCredentials(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)