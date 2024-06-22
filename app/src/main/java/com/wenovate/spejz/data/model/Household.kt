package com.wenovate.spejz.data.model

data class Household(
    val id: String = "",
    val name: String = "",
    val members: List<String> = emptyList() // List of user IDs
)