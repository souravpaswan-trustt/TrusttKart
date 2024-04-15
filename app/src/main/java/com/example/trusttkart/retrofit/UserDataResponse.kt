package com.example.trusttkart.retrofit

data class UserDataResponse(
    val success: String,
    val message: String,
    val user: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)
