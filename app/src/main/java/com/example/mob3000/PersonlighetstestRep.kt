package com.example.mob3000

import android.util.Log.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonlighetstestRep (private val apiService: ApiService) {

    suspend fun fetchScore(resultID: String): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getResults(resultID)

                response.results.filter { result ->
                    result.domain in listOf("N", "O", "A", "C", "E")
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}