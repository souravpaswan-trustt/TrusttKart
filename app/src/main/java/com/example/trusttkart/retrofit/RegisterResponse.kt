package com.example.trusttkart.retrofit

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: NewUser
)

data class NewUser(
    val id: Int,
    val name: String,
    val email: String,
    val phoneNumber: String
)

