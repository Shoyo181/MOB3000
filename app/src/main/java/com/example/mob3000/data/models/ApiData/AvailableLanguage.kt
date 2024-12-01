package com.example.mob3000.data.models.ApiData

/**
 * Data klasse som representerer API-respons.
 * Denne klassen er her bare for å fange opp dataene som kommer fra APIet.
 * Brukes ikke vidre enn det
 */
data class AvailableLanguage(
    val id: String,
    val text: String,
    val value: String
)