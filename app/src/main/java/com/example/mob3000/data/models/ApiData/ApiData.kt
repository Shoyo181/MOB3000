package com.example.mob3000.data.models.ApiData

/**
 * Data klasse som representerer API-respons.
 * Denne klassen er her bare for å fange opp dataene som kommer fra APIet.
 * Brukes ikke vidre enn det
 */
data class ApiData(
    val age: String,
    val availableLanguages: List<AvailableLanguage>,
    val gender: String,
    val language: String,
    val nationality: String,
    val results: List<Result>,
    val timestamp: String
)