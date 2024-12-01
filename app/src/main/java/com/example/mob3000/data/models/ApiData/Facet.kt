package com.example.mob3000.data.models.ApiData

/**
 * Data klasse som representerer API-respons.
 * Klassen inneholder "Del-tema" om test-resultater
 * eks, Tema: Nevrotisme. Del-tema: Angst, Sinne, osv
 */
data class Facet(
    val count: Int,
    val facet: Int,
    val score: Int,
    val scoreText: String,
    val text: String,
    val title: String
)