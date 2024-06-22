package com.wenovate.spejz.data.model

data class Grocery(
    val id: String = "",
    val name: String = "",
    val expirationDate: Long = 0L, // timestamp in milliseconds
    val userId: String = "" // userId to associate with the user
)