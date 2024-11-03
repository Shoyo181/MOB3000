package com.example.mob3000.data.model

data class Big5TestRes(
    val age: String,
    val availableLanguages: List<AvailableLanguage>,
    val gender: String,
    val language: String,
    val nationality: String,
    val results: List<Result>,
    val timestamp: Long
)