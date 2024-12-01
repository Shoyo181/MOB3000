package com.example.mob3000.data.models

/**
 * Data klasse om bruker innlogget
 * Brukes også til å opprette ny bruker i database
 */
data class Bruker (
    val id: String = "",
    val email: String = ""
)