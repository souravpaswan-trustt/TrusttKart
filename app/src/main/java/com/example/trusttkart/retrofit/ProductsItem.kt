package com.example.trusttkart.retrofit

data class ProductsItem(
    val id: Int,
    val addedOn: String,
    val imageUrl: String,
    val productDescription: String,
    val productName: String,
    val productPrice: Double
)