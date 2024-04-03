package com.example.trusttkart.retrofit

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phoneNumber: String
)

