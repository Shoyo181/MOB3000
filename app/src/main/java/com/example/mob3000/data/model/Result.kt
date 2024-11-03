package com.example.mob3000.data.model

data class Result(
    val count: Int,
    val description: String,
    val domain: String,
    val facets: List<Facet>,
    val score: Int,
    val scoreText: String,
    val shortDescription: String,
    val text: String,
    val title: String
)