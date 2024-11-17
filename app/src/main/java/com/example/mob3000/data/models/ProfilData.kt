package com.example.mob3000.data.models

data class ProfilData(
    val id: String,
    val navn: String,
    val epost: String,
    val score: List<List<Int>>
)
data class ScoreData(
    val totalScore: List<Int>,
    val nevrotisismeScore: List<Int>,
    val ekstroversjonScore: List<Int>,
    val åpenhetForErfaringerScore: List<Int>,
    val medmenneskelighetScore: List<Int>,
    val planmessighetScore: List<Int>,
)