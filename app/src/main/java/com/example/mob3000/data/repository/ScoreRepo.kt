package com.example.mob3000.data.repository

import com.example.mob3000.data.api.ScoreService

import com.example.mob3000.data.models.ScoreData
import com.example.mob3000.data.models.ScoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScoreRepo (private val scoreService: ScoreService) {

    suspend fun fetchAllScore(testID: String): List<ScoreData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = scoreService.getTestData(testID)
                response.results.filter{ result ->
                    result.domain in listOf("N", "O", "A", "C", "E")
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

}