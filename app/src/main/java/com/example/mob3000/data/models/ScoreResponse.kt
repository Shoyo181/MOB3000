package com.example.mob3000.data.models

data class ScoreService(
    val results: List<ScoreData>
)
data class ScoreData(
    val domain: String,
    val score: Score,
    val facets: List<Score>
)
data class Score(
    val score: Int,
    val title: String,
)