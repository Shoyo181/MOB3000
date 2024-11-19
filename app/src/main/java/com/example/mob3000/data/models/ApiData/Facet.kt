package com.example.mob3000.data.models.ApiData

data class Facet(
    val count: Int,
    val facet: Int,
    val score: Int,
    val scoreText: String,
    val text: String,
    val title: String
)