package com.example.trusttkart.retrofit

data class FetchCartResponse(
    val id: Int,
    val quantity: Int,
    val product: Product,
    val totalPrice: Double
)

data class Product(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val imageUrl: String,
    val addedOn: String?,
    val quantity: Int
)