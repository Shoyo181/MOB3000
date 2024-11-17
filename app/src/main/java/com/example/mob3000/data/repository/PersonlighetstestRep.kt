package com.example.mob3000.data.repository

import com.example.mob3000.data.api.ApiResponse
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.api.Result

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class PersonlighetstestRep (private val apiService: ApiService) {

    suspend fun fetchScore(resultID: String): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getResults(resultID)
                response.results.filter{ result ->
                    result.domain in listOf("N", "O", "A", "C", "E")
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}