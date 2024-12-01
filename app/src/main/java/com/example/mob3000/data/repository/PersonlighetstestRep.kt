package com.example.mob3000.data.repository

import android.util.Log
import com.example.mob3000.data.api.ApiService
import com.example.mob3000.data.models.ApiData.Result

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Klasse som håndterer henting av data fra API
 *
 * @param apiService: ApiService
 */
class PersonlighetstestRep (private val apiService: ApiService) {

    /**
     * Henter data fra API
     * Grensesnittet ApiService returnerer ApiData, men i denne funksjoen skiller vi ut
     * dette, og returnerer bare results som er en liste med Result-objekter
     *
     * @param resultID: String - ID til test
     * @param language: String - hvilket språk vi får tilbake fra API
     * @return List<Result> - liste med data fra API, brukere data klassen Result
     */
    suspend fun fetchScore(resultID: String, language: String): List<Result> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getResults(resultID, language)
                Log.d("API-test-repo", "Full respons hentet: $response")
                response.results // returnerer results fra ApiData
            } catch (e: Exception) {
                Log.e("API-test-repo", "Feil ved henting av data: ${e.message}", e)
                emptyList()
            }
        }
    }
}