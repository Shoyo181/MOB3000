package com.example.mob3000.data.repository

import android.util.Log
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.models.ApiData.Result

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class PersonlighetstestRep (private val apiService: ApiService) {

    suspend fun fetchScore(resultID: String): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getResults(resultID)
                Log.d("API-test-repo", "Full respons hentet: $response")
                response.results // returnerer all data fra API
            } catch (e: Exception) {
                Log.e("API-test-repo", "Feil ved henting av data: ${e.message}", e)
                emptyList()
            }
        }
    }
}