package com.example.mob3000.data.models

/**
 * Data klasse om score for en person/profil
 * komprimert data klasse enn den vi får fra api
 */
data class ScoreList(
    val name: String,
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