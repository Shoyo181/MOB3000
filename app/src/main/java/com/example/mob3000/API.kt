package com.example.mob3000


data class ApiResponse (
    val results: List <Result>
)
data class Result (
    val domain: String,
    val score: Int
)

