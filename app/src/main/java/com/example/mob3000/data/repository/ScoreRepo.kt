package com.example.mob3000.data.repository

import android.util.Log
import com.example.mob3000.data.api.ScoreService

import com.example.mob3000.data.models.ScoreData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScoreRepo (private val scoreService: ScoreService) {

    suspend fun fetchAllScore(testID: String): List<ScoreData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = scoreService.getScoreResults(testID)
                Log.d("API-test-repo", "Response: ${response}")

                response.results.map {
                    Log.d("API-test-repo", "Domain: ${it.domain}")
                    it
                }

                /*
                response.results.forEach { Log.d("API-test-repo", "Domain: ${it.domain}") }
                response.results.filter{ result ->
                    result.domain in listOf("N", "O", "A", "C", "E")
                }
                */

            } catch (e: Exception) {
                Log.e("API-test-repo", "Error fetching scores: ${e.message}", e)
                emptyList()
            }
        }
    }
}