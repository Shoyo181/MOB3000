package com.example.mob3000.data.models.ApiData

/**
 * Data klasse som representerer API-respons.
 * Resultat fra testen om ett Tema (eks. Nevrotisme)
 */
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