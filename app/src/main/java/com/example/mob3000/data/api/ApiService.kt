package com.example.mob3000.data.api

import com.example.mob3000.data.models.ApiData.ApiData
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Endepunkt for å hente data fra APIet til RubyNor
 *
 */
interface ApiService {

    /**
     * Henter resultater fra API-en basert på resultatet ID og språk.
     *
     * @param resultId ID-en til resultatet som skal hentes.
     * @param language Språket resultatene skal være på.
     * @return En [ApiData]-instans som inneholder resultatene.
     */
    @GET("api/result/{resultId}/{language}")
    suspend fun getResults(@Path("resultId") resultId: String,
                           @Path("language") language: String
    ): ApiData
}
