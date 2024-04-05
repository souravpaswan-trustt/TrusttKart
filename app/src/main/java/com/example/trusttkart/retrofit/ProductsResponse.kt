package com.example.trusttkart.retrofit

data class ProductsResponse(
    val id: Int,
    val productName: String,
    val productDescription: String,
    val productPrice: Double,
    val imageUrl: String,
    val addedOn: String,
    val categoryId: Int,
    val categoryType: String
)